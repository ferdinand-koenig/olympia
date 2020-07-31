package main.application;

import java.util.HashMap;

public interface IOHandler {
    /**
     * Reads from a comma separated value data base
     * @param path path to the db
     * @return HashMap with read Athletes
     */
    HashMap<Integer, Athlete> read(String path);

    /**
     * Writes to a comma separated value data base
     * @param athletes
     * @param path
     */
    void write(HashMap<Integer, Athlete> athletes, String path);
}
