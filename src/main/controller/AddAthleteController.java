package main.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import main.application.Athlete;
import main.application.Team;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

public class AddAthleteController {
    public static void showEntryForm(Stage owner, HashMap<Integer, Athlete> athletes, Scene rootScene){
        try {
            Scene formScene = new Scene(FXMLLoader.load(AddEventController.class.getResource("AddAthlete.fxml")));
            Stage formView = new Stage();

            ComboBox<String> sexComboBox = (ComboBox) formScene.lookup("#sexComboBox");
            sexComboBox.setItems(FXCollections.observableArrayList(
                    "F",
                    "M"
            ));
            sexComboBox.getSelectionModel().selectFirst();

            Spinner<Integer> heightSpinner = ((Spinner<Integer>) formScene.lookup("#heightSpinner"));
            heightSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                try{
                    if(0 > Integer.parseInt(newValue) || 300 < Integer.parseInt(newValue))
                        heightSpinner.getEditor().textProperty().set(oldValue);
                }catch(Exception e){
                    heightSpinner.getEditor().textProperty().set(oldValue);
                }
            });

            Spinner<Double> weightSpinner = ((Spinner<Double>) formScene.lookup("#weightSpinner"));
            weightSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                try{
                    if(0.0 > Double.parseDouble(newValue.replace(",", ".")) || 500.0 < Double.parseDouble(newValue.replace(",",".")))
                        weightSpinner.getEditor().textProperty().set(oldValue);
                }catch(Exception e){
                    weightSpinner.getEditor().textProperty().set(oldValue);
                }
            });


            Button submitBtn = (Button) formScene.lookup("#submitBtn");
            submitBtn.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.PRIMARY)
                    if(!((TextField) formScene.lookup("#nameTextField")).getText().isEmpty() &&
                            !((TextField) formScene.lookup("#teamTextField")).getText().isEmpty())
                        if(((TextField) formScene.lookup("#nocTextField")).getText().length()==3) {
                            getEventAndCreateAthlete(
                                    ((TextField) formScene.lookup("#nameTextField")).getText(),
                                    sexComboBox.getValue(),
                                    heightSpinner.getValue().intValue(),
                                    weightSpinner.getValue().floatValue(),
                                    new Team(((TextField) formScene.lookup("#teamTextField")).getText(), ((TextField) formScene.lookup("#nocTextField")).getText().toUpperCase()),
                                    athletes,
                                    formView,
                                    rootScene);
                            //formView.close();
                        }
            });

            formView.setTitle("Add new Athlete");
            formView.initOwner(owner);
            formView.setScene(formScene);
            formView.show();
        }catch(IOException e){
            System.err.println("Fatal: Cannot find AddAthlete.fxml");
            e.printStackTrace();
        }
    }

    private static void getEventAndCreateAthlete(String name, String sex, int height, float weight, Team team, HashMap<Integer, Athlete> athletes, Stage stage, Scene rootScene) {
        try {
            Scene formScene = new Scene(FXMLLoader.load(AddEventController.class.getResource("AddEvent.fxml")));

            ComboBox<String> seasonComboBox = (ComboBox) formScene.lookup("#seasonComboBox");
            seasonComboBox.setItems(FXCollections.observableArrayList(
                    "Summer",
                    "Winter"
            ));
            seasonComboBox.getSelectionModel().selectFirst();

            Button submitBtn = (Button) formScene.lookup("#submitBtn");
            submitBtn.setText("Next");
            submitBtn.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.PRIMARY){
                    try {
                        Integer.parseInt(((TextField) formScene.lookup("#yearTextField")).getText());
                    }catch(NumberFormatException e){
                        return;
                    }

                    int nextFreeKey = Collections.max(athletes.keySet()) +1;

                    Athlete incompleteAthlete = new Athlete(nextFreeKey, name, sex, height, weight, team, null);
                    AddEventController.getAgeAndAddParticipation(incompleteAthlete, AddEventController.submitEntryForm(formScene), stage);

                    athletes.put(nextFreeKey, incompleteAthlete);
                    ControllerUtilities.updateAthleteTable(athletes, rootScene);
                    stage.close();
                }
            });

            stage.setScene(formScene);
        }catch(IOException e){
            System.err.println("Fatal: Cannot find AddEvent.fxml");
            e.printStackTrace();
        }
    }
}
