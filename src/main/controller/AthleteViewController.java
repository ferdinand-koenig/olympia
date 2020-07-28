package main.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import main.application.Athlete;
import main.application.Medal;
import main.application.Participation;

import java.io.IOException;

public class AthleteViewController {
    private static class ParticipationListElement{
        private Participation participation;
        private Medal medal;

        ParticipationListElement(Participation participation, Medal medal){
            this.participation=participation;
            this.medal=medal;
        }

        private String medalString(){
            return medal == null ? "-" : String.valueOf(medal.getValue());
        }
    }

    public void showAthlete(Athlete athlete, Stage owner){
        try {
            Scene athleteScene = new Scene(FXMLLoader.load(AthleteViewController.class.getResource("AthleteView.fxml")));
            Stage athleteView = new Stage();

            TextFlow headText = (TextFlow) athleteScene.lookup("#nameTextFlow");
            Text athleteName = new Text(athlete.getName());
            athleteName.setFont(Font.font("Verdana", 25));
            athleteName.setTextAlignment(TextAlignment.CENTER);
            headText.getChildren().add(athleteName);
            headText.setTextAlignment(TextAlignment.CENTER);

            populateGeneralTab(athlete, athleteScene);
            populateParticipationTab(athlete, athleteScene);

            athleteView.setTitle("Athlete: ".concat(athlete.getName()));
            athleteView.initOwner(owner);
            athleteView.setScene(athleteScene);
            athleteView.showAndWait();
        }catch(IOException e){
            System.err.println("Fatal: Cannot find AthleteView.fxml");
            e.printStackTrace();
        }
    }

    private void populateGeneralTab(Athlete athlete, Scene athleteScene){
        Text idText, sexText, heightText, weightText, teamText;
        idText = new Text(Integer.toString(athlete.getId()));
        sexText = new Text(athlete.getSex());
        heightText = new Text(athlete.getHeight() == -1 ? "N/A" : Integer.toString(athlete.getHeight()));
        weightText = new Text(athlete.getWeight() == -1.0 ? "N/A" : Float.toString(athlete.getWeight()));
        teamText = new Text(athlete.getTeam().getName().concat(" (").concat(athlete.getTeam().getNoc()).concat(")"));

        GridPane detailsGridPane = (GridPane) athleteScene.lookup("#athleteDetailsGrid");
        detailsGridPane.add(idText, 1,0);
        detailsGridPane.add(sexText, 1,1);
        detailsGridPane.add(heightText, 1,2);
        detailsGridPane.add(weightText, 1,3);
        detailsGridPane.add(teamText, 1,4);
    }

    private void populateParticipationTab(Athlete athlete, Scene athleteScene){
        ObservableList<ParticipationListElement> observableParticipationList = FXCollections.observableArrayList();
        for(Participation participation : athlete.getParticipations())
                observableParticipationList.add(new ParticipationListElement(participation, athlete.wonMedalFor(participation.getEvent())));

        TableView table = (TableView) athleteScene.lookup("#participationTable");
        TableColumn<ParticipationListElement, String> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(Integer.toString(param.getValue().participation.getAge())));
        TableColumn<ParticipationListElement, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().participation.getEvent().getTitle()));
        TableColumn<ParticipationListElement, String> sportColumn = new TableColumn<>("Sport");
        sportColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().participation.getEvent().getSport()));
        TableColumn<ParticipationListElement, String> gameColumn = new TableColumn<>("Sport");
        gameColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().participation.getEvent().getGame().toString()));
        TableColumn<ParticipationListElement, String> medalColumn = new TableColumn<>("Medal");
        medalColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().medalString()));

        TableColumn<ParticipationListElement, String> eventColumn = new TableColumn<>("Event");
        eventColumn.getColumns().addAll(titleColumn, sportColumn, gameColumn);
        table.getColumns().addAll(ageColumn, eventColumn, medalColumn);
        table.setItems(observableParticipationList);
    }
}
