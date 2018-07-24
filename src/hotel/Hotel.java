package hotel;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.collections.ObservableList;

public class Hotel {
    private ArrayList<Room> rooms;
    
    public Hotel() {
        rooms = new ArrayList<Room>();
    }
    
    public void readFrom(Scanner input) {
        // Read the five rooms from the input file
        for(int n = 0;n < 5;n++) {
            Room newRoom = new Room();
            newRoom.readFrom(input);
            rooms.add(newRoom);
        }
    }
    
    public void writeTo(PrintWriter output) {
        for(Room r : rooms) {
            r.writeTo(output);
        }
    }
    
    public Room getRoom(String forName) {
        for(Room r : rooms) {
            if(r.getName().equalsIgnoreCase(forName))
                return r;
        }
        return null;
    }
    
    public ArrayList<Room> getRooms() { return rooms; }
}
