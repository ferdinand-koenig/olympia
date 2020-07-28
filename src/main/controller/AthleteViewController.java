package main.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import main.application.Athlete;
import java.io.IOException;

public class AthleteViewController {
    public static void showAthlete(Athlete athlete, Stage owner){
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

    private static void populateGeneralTab(Athlete athlete, Scene athleteScene){
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

    private static void populateParticipationTab(Athlete athlete, Scene athleteScene){

    }
}
