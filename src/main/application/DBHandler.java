package main.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class DBHandler implements IOHandler{
    public DBHandler(){
    }

    @Override
    public HashMap<Integer, Athlete> read(String path) {
        String line;
        Pattern pattern = Pattern.compile(",");
        String[] attribute;
        Object clipboard;

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
                game = (clipboard = games.putIfAbsent(Integer.toString(game.getYear()).concat(" ").concat(game.getSeason()), game)) != null ? (Game) clipboard : game;

                event = new Event(crop(attribute[13]), crop(attribute[12]), game);
                event = (clipboard = events.putIfAbsent(event.getTitle(), event)) != null ? (Event) clipboard : event;

                athlete = new Athlete(Integer.parseInt(crop(attribute[0])), removeDoubleQuotes(crop(attribute[1])), crop(attribute[2]), (attribute[4].equals("NA")) ? -1 : Integer.parseInt(attribute[4]), (attribute[5].equals("NA")) ? -1 : Float.parseFloat(attribute[5]), team, event);
                System.out.println("You are here:" + athlete.getId());
                athlete = (clipboard = athletes.putIfAbsent(athlete.getId(), athlete)) != null ? (Athlete) clipboard : athlete;
                if(!athlete.getEvents().contains(event)) athlete.addEvent(event);

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
}