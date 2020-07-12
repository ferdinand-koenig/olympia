package main.application;

public class Game {
    private int year;
    private String season, city;

    public Game(int year, String season, String city){
        setYear(year);
        setSeason(season);
        setCity(city);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString(){
        return Integer.toString(this.getYear()).concat(" ").concat(this.getSeason());
    }
}
