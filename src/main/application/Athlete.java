package main.application;

import java.util.ArrayList;
import java.util.List;

public class Athlete implements java.io.Serializable{
    private int id, height;
    private float weight;
    private String name, sex;
    private Team team; //Could be an athlete member of several teams? => No
    private final List<Medal> medals = new ArrayList<>();
    private final List<Participation> participations = new ArrayList<>();

    public Athlete(int id, String name, String sex, int height, float weight, Team team, Participation participation) {
        setId(id);
        setHeight(height);
        setWeight(weight);
        setName(name);
        setSex(sex);
        setTeam(team);
        if(participation != null) addParticipation(participation);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    private void setWeight(float weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    private void setSex(String sex) {
        this.sex = sex;
    }

    public Team getTeam() {
        return team;
    }

    private void setTeam(Team team) {
        this.team = team;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void addParticipation(Participation participation) {
        participations.add(participation);
    }

    private List<Medal> getMedals() {
        return medals;
    }

    public void addMedal(Medal medal) {
        medals.add(medal);
    }


    /**
     * Checks whether the athlete has won a medal for a given event or not
     * @param event event for which the existence of a medal is checked for
     * @return Medal associated with event. If there is none, return value will be null
     */
    public Medal wonMedalFor(Event event){
        for(Medal medal : this.getMedals())
            if(medal.getEvent().equals(event))
                return medal;
        return null;
    }
}
