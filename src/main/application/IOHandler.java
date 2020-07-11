package main.application;

import java.util.HashMap;

public interface IOHandler {
    HashMap<Integer, Athlete> read(String path);
}
