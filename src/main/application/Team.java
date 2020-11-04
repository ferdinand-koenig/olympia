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

    private void setName(String name) {
        this.name = name;
    }

    public String getNoc() {
        return noc;
    }

    private void setNoc(String noc) {
        this.noc = noc;
    }

    @Override
    public String toString(){
        return name.concat(" (").concat(noc).concat(")");
    }
}
