package main.application;

import java.io.*;
import java.util.HashMap;

public class Serializer implements IOHandler{

    @Override
    public HashMap<Integer, Athlete> read(String path) {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            HashMap<Integer, Athlete> athletes = (HashMap<Integer, Athlete>) in.readObject();
            in.close();
            fileIn.close();
            return athletes;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
            return null;
        }
    }

    @Override
    public void write(HashMap<Integer, Athlete> athletes, String path) {
        try{
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(athletes);
            out.close();
            fileOut.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}