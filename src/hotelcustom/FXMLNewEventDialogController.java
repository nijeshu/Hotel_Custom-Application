package hotelcustom;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import hotel.Event;
import java.time.LocalDate;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class FXMLNewEventDialogController implements Initializable {

    private RoomsPane rooms;
    private LocalDate date;
    private Event event1;
    @FXML
    private TextField eventNumber;
    @FXML
    private TextField customerNumber;
    @FXML
    private TextField groupSize;
    @FXML
    private TextField startTime;
    @FXML
    private TextField endTime;

    @FXML
    private void cancelDialog(ActionEvent event) {
        eventNumber.getScene().getWindow().hide();
    }
    @FXML
    private Label errorText;
    @FXML
    private Button dataOld;

    @FXML
    private Button changesSaved;

    @FXML
    private void acceptDialog(ActionEvent event) {
        int evtNumber = Integer.parseInt(eventNumber.getText());
        int custNumber = Integer.parseInt(customerNumber.getText());
        int start = Integer.parseInt(startTime.getText());
        int end = Integer.parseInt(endTime.getText());
        int size = Integer.parseInt(groupSize.getText());

        Event newEvent = new Event(evtNumber, custNumber, date, start, end, size);

        rooms.addEvent(newEvent);
        eventNumber.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setRooms(RoomsPane rooms) {
        this.rooms = rooms;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setEvent(Event a) {
        this.event1 = a;
    }

    @FXML
    public void oldData(ActionEvent e) {

        this.eventNumber.setText(String.valueOf(event1.getEventNumber()));
        this.customerNumber.setText(String.valueOf(event1.getCustomerNumber()));
        this.groupSize.setText(String.valueOf(event1.getGroupSize()));
        this.startTime.setText(String.valueOf(event1.getStartTime()));
        this.endTime.setText(String.valueOf(event1.getStartTime() + event1.getDuration()));

    }

    @FXML
    public void savedChanges(ActionEvent e) {
        rooms.deleteOldEvent();
        int evtNumber = Integer.parseInt(eventNumber.getText());
        int custNumber = Integer.parseInt(customerNumber.getText());
        int start = Integer.parseInt(startTime.getText());
        int end = Integer.parseInt(endTime.getText());
        int size = Integer.parseInt(groupSize.getText());
        Event newEvent = new Event(evtNumber, custNumber, date, start, end, size);
        rooms.editEvent(newEvent);
        eventNumber.getScene().getWindow().hide();
    }
}
