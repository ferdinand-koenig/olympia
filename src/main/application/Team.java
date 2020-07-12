package main.application;

public class Team implements java.io.Serializable{
    private String name, noc;

    public Team(String name, String noc){
        setName(name);
        setNoc(noc);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoc() {
        return noc;
    }

    public void setNoc(String noc) {
        this.noc = noc;
    }

}
