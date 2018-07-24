package hotelcustom;

import hotel.Event;
import hotel.Hotel;
import hotel.Room;
import static java.awt.SystemColor.text;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class RoomsPane extends Pane {

    private Hotel hotel;
    private ObservableList<Event> unplaced;
    LocalDate currentDate;
    double dragStart;
    EventPane selected;

    public RoomsPane(Hotel hotel, LocalDate date) {
        this.hotel = hotel;
        this.currentDate = date;
        selected = null;
        unplaced = FXCollections.observableArrayList();
        setUpRoomsForDate(currentDate);

    }

    public EventPane getSelectedPane() {
        return selected;
    }

    public Event getUnplacedSelected() {
        for (int i = 1; i < unplaced.size(); i++) {
            if (unplaced.get(i) == selected.getEvent()) {
                return unplaced.get(i);
            }

        }
        return null;
    }

    public void writeUnplaced(PrintWriter output) {
        output.println(unplaced.size());
        for (int i = 0; i < unplaced.size(); i++) {
            unplaced.get(i).writeTo(output);
        }
        // output.println("****END***");

    }

    public void readUnplaced(Scanner input) {
        String firstLine = input.nextLine();
        int howMany = Integer.parseInt(firstLine);
        for (int n = 0; n < howMany; n++) {
            Event nextEvent = new Event();
            nextEvent.readFrom(input);
            unplaced.add(nextEvent);
        }
    }

    public void setDate(LocalDate newDate) {
        this.currentDate = newDate;
        setUpRoomsForDate(currentDate);
    }

    private void setUpRoomsForDate(LocalDate date) {
        this.getChildren().clear();

        // Set up the hour scale on the left
        int hour = 7;
        int hourY = EventPane.PIXELS_PER_HOUR + 10;
        for (hour = 7; hour < 23; hour++) {
            Text label = new Text(2, hourY, String.valueOf(hour));
            this.getChildren().add(label);
            hourY += EventPane.PIXELS_PER_HOUR;
        }

        // Set up the column heading and separators
        int nameX = EventPane.LEFT_MARGIN;
        Text heading = new Text(nameX + 2, 12, "Unplaced");
        this.getChildren().add(heading);
        nameX += EventPane.EVENT_WIDTH;
        for (Room r : hotel.getRooms()) {
            Line separator = new Line(nameX - 1, 0, nameX - 1, (23 - 6) * EventPane.PIXELS_PER_HOUR);
            separator.setStroke(Color.LIGHTGRAY);
            this.getChildren().add(separator);
            heading = new Text(nameX + 2, 12, r.getName());
            this.getChildren().add(heading);
            nameX += EventPane.EVENT_WIDTH;
        }

        // Display the events
        FilteredList<Event> filteredEvents = new FilteredList<Event>(unplaced, (evt) -> evt.getDate().equals(date));
        for (Event e : filteredEvents) {
            EventPane pane = new EventPane(e, null);
            pane.setAnchorX(EventPane.LEFT_MARGIN);
            pane.setOnMousePressed((evt) -> startDrag(evt, pane));
            pane.setOnMouseDragged((evt) -> continueDrag(evt, pane));
            pane.setOnMouseReleased((evt) -> finishDrag(evt, pane));
            pane.setOnMouseClicked((evt) -> {
                if (selected != null) {
                    selected.unSelectedEventPane();
                }
                pane.selectedEventPane();
                selected = pane;
            });

            pane.setOnKeyTyped((del) -> handleKeyPress(del));
            this.getChildren().add(pane);
        }
        int x = EventPane.EVENT_WIDTH + EventPane.LEFT_MARGIN;
        for (Room r : hotel.getRooms()) {
            filteredEvents = new FilteredList<Event>(r.getEvents(), (evt) -> evt.getDate().equals(date));
            for (Event e : filteredEvents) {
                EventPane pane = new EventPane(e, r);
                pane.setAnchorX(x);
                pane.setOnMousePressed((evt) -> startDrag(evt, pane));
                pane.setOnMouseDragged((evt) -> continueDrag(evt, pane));
                pane.setOnMouseReleased((evt) -> finishDrag(evt, pane));
                pane.setOnMouseClicked((evt) -> {
                    if (selected != null) {
                        selected.unSelectedEventPane();
                    }
                    pane.selectedEventPane();
                    selected = pane;
                });

                pane.setOnKeyTyped((del) -> handleKeyPress(del));
                this.getChildren().add(pane);
            }
            x += EventPane.EVENT_WIDTH;
        }
    }

    public void handleKeyPress(KeyEvent Key) {
        System.out.println("Received key press");
        Event delete = selected.getEvent();
        if (Key.getCode() == KeyCode.BACK_SPACE) {
            for (Event a : unplaced) {
                if (selected.getEvent() == a) {
                    unplaced.remove(a);
                    getChildren().remove(selected);
                }

            }
            selected.getRoom().removeEvent(delete);
            getChildren().remove(selected);
        }

    }

    public void deleteOldEvent() {
        Event delete = selected.getEvent();
        if (selected.getRoom() == null) {
            unplaced.remove(delete);
        } else {
            selected.getRoom().removeEvent(delete);
        }
        this.setUpRoomsForDate(currentDate);
        this.getChildren().remove(selected);

    }

    public void addEvent(Event e) {
        Room newRoom = new Room();
        if (newRoom.allowsEvent(e)) {
            newRoom.addEvent(e);
        } else {
            newRoom.removeEvent(e);
            unplaced.add(e);
        }
        this.setUpRoomsForDate(currentDate);
    }

    public void editEvent(Event n) {
        if (!(selected.getRoom() == null)) {
            if (selected.getRoom().allowsEvent(n)) {
                selected.getRoom().addEvent(n);
            } else {
               
                unplaced.add(n);
            }
          
        }
        else{
            unplaced.add(n);
        }
        this.setUpRoomsForDate(currentDate);
    }

    private void startDrag(MouseEvent event, EventPane pane) {
        dragStart = event.getSceneX();
    }

    private void continueDrag(MouseEvent event, EventPane pane) {
        double dragNow = event.getSceneX();
        pane.setLayoutX(pane.getAnchorX() + dragNow - dragStart);
    }

    private void finishDrag(MouseEvent event, EventPane pane) {
        Point2D dragEnd = this.sceneToLocal(event.getSceneX(), event.getSceneY());
        Event e = pane.getEvent();
        if (dragEnd.getX() <= EventPane.EVENT_WIDTH + EventPane.LEFT_MARGIN) {
            Room currentRoom = pane.getRoom();
            if (currentRoom != null) {
                currentRoom.removeEvent(e);
                unplaced.add(e);
            }
            pane.setRoom(null);
            pane.setAnchorX(EventPane.LEFT_MARGIN);
        } else {
            int roomIndex = (int) (((dragEnd.getX() - EventPane.LEFT_MARGIN) / EventPane.EVENT_WIDTH) - 1);
            Room targetRoom = hotel.getRooms().get(roomIndex);
            Room currentRoom = pane.getRoom();
            int oldAnchor = pane.getAnchorX();
            if (targetRoom != currentRoom && targetRoom.allowsEvent(e)) {
                if (currentRoom != null) {
                    currentRoom.removeEvent(e);
                } else {
                    unplaced.remove(e);
                }
                targetRoom.addEvent(e);
                pane.setRoom(targetRoom);
                pane.setAnchorX(EventPane.LEFT_MARGIN + (roomIndex + 1) * EventPane.EVENT_WIDTH);
            } else {
                pane.setAnchorX(oldAnchor);
            }
        }
    }

    @Override
    protected double computePrefHeight(double width) {
        return (23 - 7) * EventPane.PIXELS_PER_HOUR;
    }

    @Override
    protected double computePrefWidth(double height) {
        return 6 * EventPane.EVENT_WIDTH + EventPane.LEFT_MARGIN;
    }

    @Override
    public boolean isResizable() {
        return false;
    }

}
