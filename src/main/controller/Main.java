package main.controller;

/*
import java.util.HashMap;
import java.util.Map;

public class Main{
    //Matrikelnummer.zip
    //finalize vars
    //JavaDoc fehlt
    //Handling von exceptions
    //Only adding new athletes
    //beim einfügen auch kontrolle, ob die events/games schon bestehen
    //ev. einige Variablen auf final, wenn sie sich nicht ändern
    //Participation dennoch als innerclass? Umschreiben bei anderen Klassen -> ev in Konstruktot rein. Ganz am Ende

 */


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.application.Athlete;
import main.application.DBHandler;
import main.application.IOHandler;
import main.application.Serializer;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class Main extends Application {
    private static HashMap<Integer, Athlete> athletes;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));

        Scene scene = new Scene(root, 700, 450);

        primaryStage.setTitle("Athletes");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("https://img.icons8.com/officexs/72/athlete.png"));
        primaryStage.setResizable(false);
        primaryStage.show();

        IOHandler handler = new DBHandler();
        HashMap<Integer, Athlete> modifiedAthletes = new HashMap<>();
        File db;

        athletes = ((db = loadFile(primaryStage)) == null) ? new HashMap<>() : handler.read(db.getPath());
        athletes = loadSerializedData();

        Button addBtn = (Button) scene.lookup("#addBtn");
        addBtn.setOnMouseClicked(event -> {
            if(event.getButton()== MouseButton.PRIMARY){
                showAddMenu(primaryStage, modifiedAthletes, scene);
            }
        });

        Button advSearchBtn = (Button) scene.lookup("#advSearchBtn");
        advSearchBtn.setOnMouseClicked(event -> {
            if(event.getButton()== MouseButton.PRIMARY) {
                SearchController.showSearchScene(athletes, primaryStage);
            }
        });

        Button saveBtn = (Button) scene.lookup("#saveBtn");
        saveBtn.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                File file;
                if((file = saveFile(primaryStage)) == null)
                    return;
                handler.write(athletes, file.getPath());
                (new File("athletes.ser")).delete();
            }
        });

        ControllerUtilities.fillTextFlow((TextFlow) scene.lookup("#hintTextFlow"), "Hint: Double-click on athlete for more details", 10);

        addListenerToTableItems((TableView) scene.lookup("#table"), primaryStage);
        ControllerUtilities.fillTable(ControllerUtilities.filterAthletes(athletes, (TextField) scene.lookup("#searchBar")), (TableView) scene.lookup("#table"));
    }

    private static void addListenerToTableItems(TableView table, Stage primaryStage){
        table.setRowFactory(tv -> {
            TableRow<Athlete> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    Athlete clickedAthlete = row.getItem();
                    (new AthleteViewController()).showAthlete(clickedAthlete, primaryStage);
                }
            });
            return row ;
        });
    }

    private static void showAddMenu(Stage owner, HashMap<Integer, Athlete> modifiedAthletes, Scene rootScene){
        try{
            Stage addMenu = new Stage();
            Scene addScene = new Scene(FXMLLoader.load(AthleteViewController.class.getResource("AddPopUp.fxml")), 300, 100);
            addMenu.setScene(addScene);
            Button addAthleteBtn = (Button) addScene.lookup("#addAthleteBtn");
            Button addEventBtn = (Button) addScene.lookup("#addEventBtn");

            addAthleteBtn.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.PRIMARY){
                    addMenu.close();
                    AddAthleteController.showEntryForm(owner, athletes, modifiedAthletes, rootScene);
                }
            });

            addEventBtn.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.PRIMARY){
                    addMenu.close();
                    AddEventController.showEntryForm(owner, athletes, modifiedAthletes);
                }
            });

            addMenu.setTitle("Add...");
            addMenu.initOwner(owner);
            addMenu.setResizable(false);
            addMenu.showAndWait();
        }catch(IOException e){
            System.err.println("Fatal: Cannot find AddPopUp.fxml");
            e.printStackTrace();
        }
    }

    private static File loadFile(Stage owner){
        final FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "CSV database (*.csv, *.db)", "*.csv", "*.db");
        fc.getExtensionFilters().add(filter);
        fc.setTitle("Choose an athlete database");
        return fc.showOpenDialog(owner);
    }

    private static File saveFile(Stage owner){
        final FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "CSV database (*.csv, *.db)", "*.csv", "*.db");
        fc.getExtensionFilters().add(filter);
        fc.setTitle("Specify a file to save");
        return fc.showSaveDialog(owner);
    }

    private static HashMap<Integer, Athlete> loadSerializedData(){
        if(!(new File("athletes.ser")).exists()) return athletes;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Found automatically saved athletes...");
        alert.setHeaderText("Do you want to load your unsaved athletes from the last time?");
        alert.setContentText("Choose wisely!");

        ButtonType buttonTypeConfirm = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("No, delete unsaved changes", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeConfirm, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeConfirm){
            HashMap<Integer, Athlete> modifiedAthletes = (new Serializer()).read("athletes.ser");
            athletes.putAll(modifiedAthletes);
        }else
            (new File("athletes.ser")).delete();

        return athletes;
    }
}