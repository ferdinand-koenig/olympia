package main.application;

import java.util.ArrayList;
import java.util.List;

public class Athlete{
    private int id, height;
    private float weight;
    private String name, sex;
    private Team team; //Could be an athlete member of several teams? => No
    private List<Event> events = new ArrayList<>(); //Can be part of several
    private List<Medal> medals = new ArrayList<>();
    //Dont forget the age

    public Athlete(int id, String name, String sex, int height, float weight, Team team, Event event) {
        setId(id);
        setHeight(height);
        setWeight(weight);
        setName(name);
        setSex(sex);
        setTeam(team);
        getEvents().add(event);
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

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event){
        events.add(event);
    }

    public List<Medal> getMedals() {
        return medals;
    }

    public void addMedal(Medal medal) {
        medals.add(medal);
    }

    public void debug(){
        System.out.println(id + ": " + name + " (" + height + ", " + sex + ", " + weight + ") Member of Team: " + team.getName() + " in " + team.getNoc());
    }

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
