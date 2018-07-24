
package hotelcustom;

import hotel.Room;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HotelCustom extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = (Parent)loader.load();
            
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Reservation System");
        stage.show();
        
        FXMLDocumentController controller = (FXMLDocumentController) loader.getController();
        RoomsPane rooms = controller.getRoomsPane();
        scene.setOnKeyPressed((evt)->rooms.handleKeyPress(evt));
   }

    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
}

