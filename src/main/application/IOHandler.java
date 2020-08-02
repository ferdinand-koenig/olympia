package main.application;

import java.util.HashMap;

public interface IOHandler {
    /**
     * Reads from a data base
     * @param path path to the db
     * @return HashMap with read Athletes
     */
    HashMap<Integer, Athlete> read(String path);

    /**
     * Writes to a data base
     * @param athletes Athletes to save
     * @param path path to the db
     */
    void write(HashMap<Integer, Athlete> athletes, String path);
}
