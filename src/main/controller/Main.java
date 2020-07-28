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
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import main.application.Athlete;
import main.application.DBHandler;
import main.application.IOHandler;

import java.io.IOException;
import java.util.*;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));

        Scene scene = new Scene(root, 800, 500);

        primaryStage.setTitle("Athletes");
        primaryStage.setScene(scene);


        IOHandler handler = new DBHandler();
        HashMap<Integer, Athlete> athletes = new HashMap<>();

        athletes = handler.read("C:\\Users\\koenigf\\OneDrive - Hewlett Packard Enterprise\\DHBW\\1. Year\\2. Semester\\Programming II\\Projekt\\olympic.db");

        /*final ObservableList<Athlete> data = FXCollections.observableArrayList();
        data.addAll(athletes.values());*/

        Button addBtn = (Button) scene.lookup("#addBtn");
        HashMap<Integer, Athlete> finalAthletes = athletes;
        addBtn.setOnMouseClicked(event -> {
            if(event.getButton()== MouseButton.PRIMARY){
                showAddMenu(primaryStage, finalAthletes);
            }
        });

        addListenerToTableItems((TableView) scene.lookup("#table"), primaryStage);
        fillTable(filterAthletes(athletes, (TextField) scene.lookup("#searchBar")), (TableView) scene.lookup("#table"));
        primaryStage.show();
    }

    private FilteredList<Athlete> filterAthletes(HashMap<Integer, Athlete> athletes, TextField searchBar){
        ObservableList<Athlete> observableAthleteList = FXCollections.observableArrayList();
        observableAthleteList.addAll(athletes.values());
        FilteredList<Athlete> filteredAthletes = new FilteredList<>(observableAthleteList, p -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAthletes.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                if (person.getName().toLowerCase().contains(newValue.toLowerCase()))
                    return true;
                if(Integer.toString(person.getId()).contains(newValue))
                    return true;
                return false;
            });
        });

        return filteredAthletes;
    }

    protected static void fillTable(FilteredList athletes, TableView table){
        TableColumn idColumn = (TableColumn) table.getColumns().get(0);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn nameColumn = (TableColumn) table.getColumns().get(1);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Athlete, String> teamColumn = (TableColumn) table.getColumns().get(2);
        teamColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getTeam().getName()));
        table.setItems(athletes);
    }

    private void addListenerToTableItems(TableView table, Stage primaryStage){
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

    private void showAddMenu(Stage owner, HashMap<Integer, Athlete> athletes){
        try{
            Stage addMenu = new Stage();
            Scene addScene = new Scene(FXMLLoader.load(AthleteViewController.class.getResource("AddPopUp.fxml")), 300, 100);
            addMenu.setScene(addScene);
            Button addAthleteBtn = (Button) addScene.lookup("#addAthleteBtn");
            Button addEventBtn = (Button) addScene.lookup("#addEventBtn");

            addAthleteBtn.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.PRIMARY){
                    //showAddMenu();
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
}