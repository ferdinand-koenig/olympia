package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import main.application.*;

import java.io.IOException;
import java.util.HashMap;

public class AddEventController {
    public static void showEntryForm(Stage owner, HashMap<Integer, Athlete> athletes){
        try {
            Scene formScene = new Scene(FXMLLoader.load(AddEventController.class.getResource("AddEvent.fxml")));
            Stage formView = new Stage();

            ComboBox<String> seasonComboBox = (ComboBox) formScene.lookup("#seasonComboBox");
            seasonComboBox.setItems(FXCollections.observableArrayList(
                    "Summer",
                    "Winter"
            ));
            seasonComboBox.getSelectionModel().selectFirst();

            Button submitBtn = (Button) formScene.lookup("#submitBtn");
            submitBtn.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.PRIMARY){
                    athleteSelection(submitEntryForm(formScene), athletes, formView);
                }
            });

            formView.setTitle("Add new Event");
            formView.initOwner(owner);
            formView.setScene(formScene);
            formView.show();
        }catch(IOException e){
            System.err.println("Fatal: Cannot find AddEvent.fxml");
            e.printStackTrace();
        }
    }

    private static Event submitEntryForm(Scene formScene){
        String title = ((TextField) formScene.lookup("#titleTextField")).getText(),
                sport = ((TextField) formScene.lookup("#sportTextField")).getText(),
                year = ((TextField) formScene.lookup("#yearTextField")).getText(),
                season = ((ComboBox<String>) formScene.lookup("#seasonComboBox")).getValue(),
                city = ((TextField) formScene.lookup("#cityTextField")).getText();
        return new Event(title, sport, new Game(Integer.parseInt(year), season, city));
    }

    private static void athleteSelection(Event event, HashMap<Integer, Athlete> athletes, Stage stage){
        try{
            Scene athleteSelectionScene = new Scene(FXMLLoader.load(AddEventController.class.getResource("AddAthletesToEvent.fxml")));

            TableView table = (TableView) athleteSelectionScene.lookup("#table");
            Main.fillTable(new FilteredList(FXCollections.observableArrayList(athletes.values())), table);
            table.getSelectionModel().setSelectionMode(
                    SelectionMode.MULTIPLE
            );

            Button submitBtn = (Button) athleteSelectionScene.lookup("#submitBtn");
            submitBtn.setOnMouseClicked(evt -> {
                if(evt.getButton() == MouseButton.PRIMARY){
                    ObservableList<Athlete> selectedAthletes = table.getSelectionModel().getSelectedItems();
                    if(!selectedAthletes.isEmpty()){
                       selectedAthletes.forEach(athlete -> {
                           getAgeAndAddParticipation(athlete, event, stage);
                       });
                       stage.close();
                    }
                }
            });

            stage.setScene(athleteSelectionScene);
        }catch(IOException e){
            System.err.println("Fatal: Cannot find AddAthleteToEvent.fxml");
            e.printStackTrace();
        }
    }

    private static void getAgeAndAddParticipation(Athlete athlete, Event event, Stage owner){
        try{
            Stage stage = new Stage();
            Scene ageEntryScene = new Scene(FXMLLoader.load(AddEventController.class.getResource("AddAge.fxml")));

            TextFlow headText = (TextFlow) ageEntryScene.lookup("#messageTextFlow");
            Text athleteName = new Text("Input Age for: ".concat(athlete.getName()));
            athleteName.setFont(Font.font("Verdana", 25));
            athleteName.setTextAlignment(TextAlignment.CENTER);
            headText.getChildren().add(athleteName);
            headText.setTextAlignment(TextAlignment.CENTER);

            ComboBox<String> medalComboBox = (ComboBox) ageEntryScene.lookup("#medalComboBox");
            ObservableList<String> medalValues = FXCollections.observableArrayList();
            medalValues.add("None");
            for(Medal.Value value : Medal.Value.values())
                medalValues.add(value.toString());
            medalComboBox.setItems(medalValues);
            medalComboBox.getSelectionModel().selectFirst();

            ageEntryScene.lookup("#submitBtn").setOnMouseClicked(evt -> {
                if(evt.getButton() == MouseButton.PRIMARY){
                    athlete.addParticipation(new Participation(((Spinner<Integer>) ageEntryScene.lookup("#ageSpinner")).getValue().intValue(), event));
                    for(Medal.Value value : Medal.Value.values())
                        if(value.toString().equals(medalComboBox.getValue())){
                            athlete.addMedal(new Medal(value, event));
                            break;
                        }
                    stage.close();
                    }
                });

            stage.setScene(ageEntryScene);
            stage.initOwner(owner);
            stage.showAndWait();
        }catch(IOException e){
            System.err.println("Fatal: Cannot find AddAge.fxml");
            e.printStackTrace();
        }
    }
}