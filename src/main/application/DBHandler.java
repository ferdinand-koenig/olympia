package main.application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DBHandler implements IOHandler{
    public DBHandler(){
    }

    @Override
    public HashMap<Integer, Athlete> read(String path) {
        // validity of db
        String line;
        Pattern pattern = Pattern.compile(",");
        String[] attribute;
        Object clipboard;
        Participation participation;

        HashMap<Integer, Athlete> athletes = new HashMap<>();
        Athlete athlete;

        HashMap<String, Team> teams = new HashMap<>();
        Team team;

        HashMap<String, Event> events = new HashMap<>();
        Event event;

        HashMap<String, Game> games = new HashMap<>();
        Game game;

        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            in.readLine(); //Forget first line  //what happens if file is empty
            while((line = in.readLine()) != null) {
                attribute = fixSplit(pattern.split(line));

                team = new Team(crop(attribute[6]), crop(attribute[7]));
                team = (clipboard = teams.putIfAbsent(team.getName(), team)) != null ? (Team) clipboard : team;

                game = new Game(Integer.parseInt(attribute[9]), crop(attribute[10]), crop(attribute[11]));
                game = (clipboard = games.putIfAbsent(game.toString(), game)) != null ? (Game) clipboard : game;

                event = new Event(crop(attribute[13]), crop(attribute[12]), game);
                event = (clipboard = events.putIfAbsent(event.getDescription(), event)) != null ? (Event) clipboard : event;

                participation = new Participation(attribute[3].equals("NA") ? -1 : Integer.parseInt(attribute[3]), event);

                athlete = new Athlete(Integer.parseInt(crop(attribute[0])), removeDoubleQuotes(crop(attribute[1])), crop(attribute[2]), (attribute[4].equals("NA")) ? -1 : Integer.parseInt(attribute[4]), (attribute[5].equals("NA")) ? -1 : Float.parseFloat(attribute[5]), team, participation);
                System.out.println("You are here:" + athlete.getId());
                athlete = (clipboard = athletes.putIfAbsent(athlete.getId(), athlete)) != null ? (Athlete) clipboard : athlete;
                if(!athlete.getParticipations().contains(participation)) athlete.addParticipation(participation);

                if(!attribute[14].equals("NA"))
                    for(Medal.Value value : Medal.Value.values())
                        if(value.toString().equals(crop(attribute[14]))){
                            athlete.addMedal(new Medal(value, event));
                            break;
                        }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return athletes;
    }

    @Override
    public void write(HashMap<Integer, Athlete> athletes, String path) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))){
            writer.write("\"ID\",\"Name\",\"Sex\",\"Age\",\"Height\",\"Weight\",\"Team\",\"NOC\",\"Games\",\"Year\",\"Season\",\"City\",\"Sport\",\"Event\",\"Medal\"\n");

            for(Map.Entry<Integer, Athlete> athleteEntry : athletes.entrySet())
                for(String entry : makeCsvEntries(athleteEntry.getValue()))
                    writer.append(entry);
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private String crop(String string){
        if(string == null) return null;
        if(string.length() <= 2) return "";
        return string.substring(1, string.length() - 1);
    }

    private String[] fixSplit(String[] part){
        if(part == null) return null;
        int j, k;
        for(int i=0; i<part.length; i++){
            if(part[i].charAt(0) == '\"' && part[i].charAt(part[i].length()-1) != '\"'){
                j = i;
                while(part[j].charAt(part[j].length()-1) != '\"' && (j + 1 < part.length)) {
                    part[j]=part[j].concat(",").concat(part[j + 1]);
                    k = j+1;
                    while(k<part.length-1) {
                        part[k] = part[k + 1];
                        k++;
                    }
                }
            }
        }
        return part;
    }

    private String removeDoubleQuotes(String string){
        return string == null ? null : string.replace("\"\"", "\"");
    }

    private ArrayList<String> makeCsvEntries(Athlete athlete){
        ArrayList<String> entries = new ArrayList<>();
        String personPartOne, personPartTwo, entry, weight;
        Medal medal;

        personPartOne = wrap(Integer.toString(athlete.getId())).concat(",");
        personPartOne = personPartOne.concat(wrap(addDoubleQuotes(athlete.getName()))).concat(",");
        personPartOne = personPartOne.concat(wrap(athlete.getSex())).concat(",");
        personPartTwo = athlete.getHeight() == -1 ? "NA" : Integer.toString(athlete.getHeight()).concat(",");
        if(Float.compare(athlete.getWeight(), -1.0f) == 0){
            weight = "NA";
        }else{
            weight = isInteger(athlete.getWeight()) ? Integer.toString((int) athlete.getWeight()) : Float.toString(athlete.getWeight());
        }
        personPartTwo = personPartTwo.concat(weight).concat(",");

        personPartTwo = personPartTwo.concat(wrap(athlete.getTeam().getName())).concat(",");
        personPartTwo = personPartTwo.concat(wrap(athlete.getTeam().getNoc())).concat(",");

        //sortieren fehlt //Anzahl passt ned
        for(Participation participation : athlete.getParticipations()){
            entry = wrap(participation.getEvent().getGame().toString()).concat(",");
            entry = entry.concat(Integer.toString(participation.getEvent().getGame().getYear())).concat(",");
            entry = entry.concat(wrap(participation.getEvent().getGame().getSeason())).concat(",");
            entry = entry.concat(wrap(participation.getEvent().getGame().getCity())).concat(",");
            entry = entry.concat(wrap(participation.getEvent().getSport())).concat(",");
            entry = entry.concat(wrap(participation.getEvent().getTitle())).concat(",");
            entry = entry.concat((medal = athlete.wonMedalFor(participation.getEvent())) == null ? "NA" : wrap(medal.getValue().toString())).concat("\n");
            entries.add(personPartOne.concat(participation.getAge() == -1 ? "NA" : Integer.toString(participation.getAge())).concat(",").concat(personPartTwo).concat(entry));
        }
        return entries;
    }

    private String wrap(String string){
        return string == null ? null : "\"".concat(string).concat("\"");
    }

    private String addDoubleQuotes(String string){
        return string == null ? null : string.replace("\"", "\"\"");
    }

    private boolean isInteger(float f){
        return (f == (int) f);
    }
}