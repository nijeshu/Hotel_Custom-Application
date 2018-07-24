package hotel;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Room {
    private String name;
    private int capacity;
    private ObservableList<Event> events;
    
    public Room() { events = FXCollections.observableArrayList(); }
    
    public String getName() { return name; }
    
    public boolean allowsEvent(Event newEvent) {
        if(newEvent.getGroupSize() > capacity) 
            return false;
        for(Event e : events) {
            if(e.compareTo(newEvent) == 0)
                return false;
        }
        return true;
    }
    
    public void addEvent(Event newEvent) {
        events.add(newEvent);
        FXCollections.sort(events);
    }
    
    public void removeEvent(Event toRemove) {
        events.remove(toRemove);
    }
    
    
    public void readFrom(Scanner input) {
        // Read the room details and events from the file
        name = input.next() + input.nextLine();
        capacity = input.nextInt();
        int howManyEvents = input.nextInt();
        for(int n = 0;n < howManyEvents;n++) {
            Event nextEvent = new Event();
            nextEvent.readFrom(input);
            events.add(nextEvent);
        }
        FXCollections.sort(events);
    }
    
    public void writeTo(PrintWriter output) {
        output.println(name);
        output.println(capacity);
        int howManyEvents = events.size();
        output.println(howManyEvents);
        for(int n = 0;n < howManyEvents;n++)
            events.get(n).writeTo(output);
    }
    
    public ObservableList<Event> getEvents() { return events; }
}
