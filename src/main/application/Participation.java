package main.application;

public class Participation implements java.io.Serializable{
    private final int age;
    private final Event event;

    public Participation(int age, Event event){
        this.age = age;
        this.event = event;
    }

    public int getAge() {
        return age;
    }

    public Event getEvent() {
        return event;
    }
}
