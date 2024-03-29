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

class AddAthleteController {
    /**
     * Starts the process of creating a new athlete as part of the front-end. <p>
     * The Steps in the user experience are Step 1: showEntryForm(...) -> Step 2: getEventAndCreateAthlete(...) -> Step 3: AddEventController.getAgeAndAddParticipation(...)
     * Explicitly in this function the user is asked to input general attributes of the athlete such as name.
     * @param owner Stage or Window that will own the created pop-up
     * @param athletes HashMap of existing Athletes
     * @param rootScene is passed to getEventAndCreateAthlete(...) for updating the table view of athletes in this scene
     */
    static void showEntryForm(Stage owner, HashMap<Integer, Athlete> athletes, HashMap<Integer, Athlete> modifiedAthletes, Scene rootScene){
        try {
            Scene formScene = new Scene(FXMLLoader.load(AddEventController.class.getResource("AddAthlete.fxml")));
            Stage formView = new Stage();

            @SuppressWarnings("unchecked") ComboBox<String> sexComboBox = (ComboBox<String>) formScene.lookup("#sexComboBox");
            sexComboBox.setItems(FXCollections.observableArrayList(
                    "F",
                    "M"
            ));
            sexComboBox.getSelectionModel().selectFirst();

            @SuppressWarnings("unchecked") Spinner<Integer> heightSpinner = ((Spinner<Integer>) formScene.lookup("#heightSpinner"));
            heightSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue.isBlank()) {
                    heightSpinner.getEditor().textProperty().setValue("-1");
                } else {
                    try {
                        if (-1 > Integer.parseInt(newValue) || 300 < Integer.parseInt(newValue))
                            heightSpinner.getEditor().textProperty().set(oldValue);
                    } catch (Exception e) {
                        heightSpinner.getEditor().textProperty().set(oldValue);
                    }
                }
            });

            @SuppressWarnings("unchecked") Spinner<Double> weightSpinner = ((Spinner<Double>) formScene.lookup("#weightSpinner"));
            weightSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue.isBlank()){
                    weightSpinner.getEditor().textProperty().setValue("-1,0");
                }else if(newValue.equals("-0,5")) {
                    weightSpinner.getEditor().textProperty().setValue("0");
                }else{
                    try {
                        if (-1.0 > Double.parseDouble(newValue.replace(",", ".")) || 500.0 < Double.parseDouble(newValue.replace(",", ".")) || !(Double.parseDouble(newValue.replace(",", ".")) % 0.5 == 0)) {
                            weightSpinner.getEditor().textProperty().set(oldValue);
                        }
                    } catch (Exception e) {
                        weightSpinner.getEditor().textProperty().set(oldValue);
                    }
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
                                    heightSpinner.getValue(),
                                    weightSpinner.getValue().floatValue(),
                                    new Team(((TextField) formScene.lookup("#teamTextField")).getText(), ((TextField) formScene.lookup("#nocTextField")).getText().toUpperCase()),
                                    athletes,
                                    modifiedAthletes,
                                    formView,
                                    rootScene);
                        }
            });

            formView.setTitle("Add new Athlete");
            formView.initOwner(owner);
            formView.setScene(formScene);
            formView.setMinHeight(250);
            formView.setMinWidth(200);
            formView.show();
        }catch(IOException e){
            System.err.println("Fatal: Cannot find AddAthlete.fxml");
            e.printStackTrace();
        }
    }

    private static void getEventAndCreateAthlete(String name, String sex, int height, float weight, Team team, HashMap<Integer, Athlete> athletes, HashMap<Integer, Athlete> modifiedAthletes, Stage stage, Scene rootScene) {
        try {
            Scene formScene = new Scene(FXMLLoader.load(AddEventController.class.getResource("AddEvent.fxml")));

            @SuppressWarnings("unchecked") ComboBox<String> seasonComboBox = (ComboBox<String>) formScene.lookup("#seasonComboBox");
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

                    int nextFreeKey = athletes.isEmpty() ? 1 : Collections.max(athletes.keySet()) +1;

                    Athlete incompleteAthlete = new Athlete(nextFreeKey, name, sex, height, weight, team, null);
                    AddEventController.getAgeAndAddParticipation(incompleteAthlete, modifiedAthletes, AddEventController.submitEntryForm(formScene), stage);

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
