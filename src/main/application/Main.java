package main.application;

import java.util.HashMap;
import java.util.Map;

public class Main{

    public static void main(String[] args){
        IOHandler handler = new DBHandler();
        HashMap<Integer, Athlete> athletes = handler.read("C:\\Users\\koenigf\\OneDrive - Hewlett Packard Enterprise\\DHBW\\1. Year\\2. Semester\\Programming II\\Projekt\\olympic.db");

        for(Map.Entry<Integer, Athlete> athleteEntry : athletes.entrySet()){
            athleteEntry.getValue().debug();
        }
    }


}

/*import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class main.application.Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
*/