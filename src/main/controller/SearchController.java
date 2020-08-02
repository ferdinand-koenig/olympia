package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.application.Athlete;
import java.io.IOException;
import java.util.HashMap;

public class SearchController {
    /**
     * Handles and creates the search pop-up
     * @param athletes All athletes
     * @param owner The logical owner window of the new pop-up
     */
    public static void showSearchScene(HashMap<Integer, Athlete> athletes, Stage owner){
        Stage stage = new Stage();
        try {
            Scene searchScene = new Scene(FXMLLoader.load(SearchController.class.getResource("Search.fxml")));

            ComboBox<String> categoryComboBox = (ComboBox) searchScene.lookup("#categoryComboBox");
            categoryComboBox.setItems(FXCollections.observableArrayList(
                    "Team",
                    "Sport",
                    "Event",
                    "Game"
            ));

            ListView list = (ListView) searchScene.lookup("#listView");
            HashMap<String, String> entries = new HashMap<>();
            categoryComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                    entries.clear();
                    switch (newValue){
                        case "Team" ->
                            athletes.forEach((integer, athlete) ->
                                entries.putIfAbsent(athlete.getTeam().toString(), athlete.getTeam().toString())
                            );
                        case "Sport" ->
                            athletes.forEach((integer, athlete) ->
                                athlete.getParticipations().forEach(participation ->
                                    entries.putIfAbsent(participation.getEvent().getSport(), participation.getEvent().getSport())
                                )
                            );
                        case "Event" ->
                            athletes.forEach((integer, athlete) ->
                                athlete.getParticipations().forEach(participation ->
                                    entries.putIfAbsent(participation.getEvent().getTitle(), participation.getEvent().getTitle())
                                )
                            );
                        case "Game" ->
                            athletes.forEach((integer, athlete) ->
                                    athlete.getParticipations().forEach(participation ->
                                            entries.putIfAbsent(participation.getEvent().getGame().toString(), participation.getEvent().getGame().toString())
                                    )
                            );
                        }
                    list.setItems(filterList(entries, (TextField) searchScene.lookup("#searchBar")));
                }
            );

            stage.setTitle("Advanced Search");
            stage.setScene(searchScene);
            stage.initOwner(owner);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Fatal: Cannot find Search.fxml");
            e.printStackTrace();
        }
    }

    private static FilteredList<String> filterList(HashMap<String, String> entries, TextField searchBar) {
        FilteredList<String> filteredEntries = new FilteredList<>(FXCollections.observableArrayList(entries.values()), p -> true);

        String searchString = searchBar.getText();
        filteredEntries.setPredicate(string -> {
            if (searchString == null || searchString.isEmpty())
                return true;
            return string.toLowerCase().contains(searchString.toLowerCase());
        });

        searchBar.textProperty().addListener((observable, oldValue, newValue) ->
            filteredEntries.setPredicate(string -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                return string.toLowerCase().contains(newValue.toLowerCase());
            })
        );

        return filteredEntries;
    }
}
