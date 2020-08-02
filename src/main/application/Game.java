package main.application;

public class Game implements java.io.Serializable{
    private int year;
    private String season, city;

    public Game(int year, String season, String city){
        setYear(year);
        setSeason(season);
        setCity(city);
    }

    int getYear() {
        return year;
    }

    private void setYear(int year) {
        this.year = year;
    }

    String getSeason() {
        return season;
    }

    private void setSeason(String season) {
        this.season = season;
    }

    String getCity() {
        return city;
    }

    private void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString(){
        return Integer.toString(this.getYear()).concat(" ").concat(this.getSeason());
    }
}
