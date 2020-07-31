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
    public static void main(String[] args){
        IOHandler handler = new DBHandler();
        IOHandler serializer = new Serializer();
        HashMap<Integer, Athlete> athletes, newAthletes = new HashMap<>();

        athletes = handler.read("C:\\Users\\koenigf\\OneDrive - Hewlett Packard Enterprise\\DHBW\\1. Year\\2. Semester\\Programming II\\Projekt\\olympic.db");
        System.out.println("First Message");
        //HashMap<Integer, Athlete> athletes = serializer.read("C:\\Users\\koenigf\\OneDrive - Hewlett Packard Enterprise\\DHBW\\1. Year\\2. Semester\\Programming II\\Projekt\\athletes.ser");

        for(Map.Entry<Integer, Athlete> athleteEntry : athletes.entrySet()){
            athleteEntry.getValue().debug();
        }
        newAthletes.put(140000, new Athlete(140000, "Max Mustermann", "M", 184, 78, new Team("World", "WRD"), new Participation(24, new Event("Hallo", "Wettessen", new Game(2020, "Summer", "Stuttgart")))));
        //handler.write(athletes, "C:\\Users\\koenigf\\OneDrive - Hewlett Packard Enterprise\\DHBW\\1. Year\\2. Semester\\Programming II\\Projekt\\olympia - Test.db");
        serializer.write(newAthletes, "C:\\Users\\koenigf\\OneDrive - Hewlett Packard Enterprise\\DHBW\\1. Year\\2. Semester\\Programming II\\Projekt\\athletes.ser");

        System.out.println("newOne");
        newAthletes= serializer.read("C:\\Users\\koenigf\\OneDrive - Hewlett Packard Enterprise\\DHBW\\1. Year\\2. Semester\\Programming II\\Projekt\\athletes.ser");
        for(Map.Entry<Integer, Athlete> athleteEntry : newAthletes.entrySet()){
            athleteEntry.getValue().debug();
        }
    }

    /*
        Fragen:
            - Is JavaDoc also necessary at obvious functions like getAge()?
                Nein, bei anderen schon (Getter und Setter nicht)
            - Do we have to check validity of the db?
                Invalider wird die Datenbank nicht
            - Können sich Gewicht und Höhe ändern?
                Ja
            - Wenn Funktion nie null bekommt, dann abfangen?
                Nein, muss man nicht
     */


//}


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.application.Athlete;
import main.application.DBHandler;
import main.application.IOHandler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main extends Application {
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
        HashMap<Integer, Athlete> athletes;
        File db;

        athletes = ((db = loadFile(primaryStage)) == null) ? new HashMap<>() : handler.read(db.getPath());
        //athletes = handler.read("C:\\Users\\koenigf\\OneDrive - Hewlett Packard Enterprise\\DHBW\\1. Year\\2. Semester\\Programming II\\Projekt\\olympic.db");

        Button addBtn = (Button) scene.lookup("#addBtn");
        addBtn.setOnMouseClicked(event -> {
            if(event.getButton()== MouseButton.PRIMARY){
                showAddMenu(primaryStage, athletes, scene);
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

    private static void showAddMenu(Stage owner, HashMap<Integer, Athlete> athletes, Scene rootScene){
        try{
            Stage addMenu = new Stage();
            Scene addScene = new Scene(FXMLLoader.load(AthleteViewController.class.getResource("AddPopUp.fxml")), 300, 100);
            addMenu.setScene(addScene);
            Button addAthleteBtn = (Button) addScene.lookup("#addAthleteBtn");
            Button addEventBtn = (Button) addScene.lookup("#addEventBtn");

            addAthleteBtn.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.PRIMARY){
                    addMenu.close();
                    AddAthleteController.showEntryForm(owner, athletes, rootScene);
                }
            });

            addEventBtn.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.PRIMARY){
                    addMenu.close();
                    AddEventController.showEntryForm(owner, athletes);
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

    private static File loadFile(Stage owner){ //FX filechooser
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
}