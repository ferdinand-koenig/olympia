package main.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import main.application.Athlete;

import java.util.HashMap;

public class ControllerUtilities {
    /**
     *
     * @param athletes
     * @param searchBar
     * @return
     */
    protected static FilteredList<Athlete> filterAthletes(HashMap<Integer, Athlete> athletes, TextField searchBar){
        ObservableList<Athlete> observableAthleteList = FXCollections.observableArrayList();
        observableAthleteList.addAll(athletes.values());
        FilteredList<Athlete> filteredAthletes = new FilteredList<>(observableAthleteList, p -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> setPredicate(filteredAthletes, newValue));

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

    protected static void updateAthleteTable(HashMap<Integer, Athlete> athletes, Scene scene){
        TextField searchBar = (TextField) scene.lookup("#searchBar");
        FilteredList<Athlete> filteredAthletes = ControllerUtilities.filterAthletes(athletes, searchBar);

        ((TableView) scene.lookup("#table")).setItems(filteredAthletes);
        ((TableView) scene.lookup("#table")).refresh();

        String newValue = searchBar.getText();
        setPredicate(filteredAthletes, newValue);
    }

    protected static void fillTextFlow(TextFlow textFlow, String text, int fontSize){
        Text innerText = new Text(text);
        innerText.setFont(Font.font("Verdana", fontSize));
        innerText.setTextAlignment(TextAlignment.CENTER);
        textFlow.getChildren().add(innerText);
        textFlow.setTextAlignment(TextAlignment.CENTER);
    }

    private static void setPredicate(FilteredList<Athlete> filteredAthletes, String newValue) {
        filteredAthletes.setPredicate(person -> {
            if (newValue == null || newValue.isEmpty())
                return true;
            if (person.getName().toLowerCase().contains(newValue.toLowerCase()))
                return true;
            if(Integer.toString(person.getId()).contains(newValue))
                return true;
            return false;
        });
    }
}