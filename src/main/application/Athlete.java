package main.application;

import java.util.ArrayList;
import java.util.List;

public class Athlete implements java.io.Serializable{
    private int id, height;
    private float weight;
    private String name, sex;
    private Team team; //Could be an athlete member of several teams? => No
    private List<Medal> medals = new ArrayList<>();
    private List<Participation> participations = new ArrayList<>();

    public Athlete(int id, String name, String sex, int height, float weight, Team team, Participation participation) {
        setId(id);
        setHeight(height);
        setWeight(weight);
        setName(name);
        setSex(sex);
        setTeam(team);
        addParticipation(participation);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void addParticipation(Participation participation) {
        participations.add(participation);
    }

    public List<Medal> getMedals() {
        return medals;
    }

    public void addMedal(Medal medal) {
        medals.add(medal);
    }


    //auf jeden Fall löschen
    public void debug(){
        System.out.println(id + ": " + name + " (" + height + ", " + sex + ", " + weight + ") Member of Team: " + team.getName() + " in " + team.getNoc());
    }

    public Medal wonMedalFor(Event event){
        for(Medal medal : this.getMedals())
            if(medal.getEvent().equals(event))
                return medal;
        return null;
    }

    //eventuell weglassen
    @Override
    public int hashCode(){
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Athlete)) return false;

        return ((Athlete) o).hashCode() == this.hashCode() && ((Athlete) o).sex.equals(this.sex) && ((Athlete) o).height == this.height && ((Athlete) o).name.equals(this.name) && ((Athlete) o).weight == this.weight;
    }
}
