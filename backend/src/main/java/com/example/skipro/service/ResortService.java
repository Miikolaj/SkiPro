package com.example.skipro.service;

import com.example.skipro.model.Resort;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Service responsible for managing resorts and persisting them to a file.
 */
public class ResortService {
    /**
     * Set containing all resorts.
     */
    private Set<Resort> resorts = new HashSet<>();
    /**
     * Name of the file used for saving resorts.
     */
    private static final String FILE_NAME = "/data/resorts.ser";

    /**
     * Constructs a ResortService and loads resorts from file.
     */
    public ResortService() {
        try {
            loadResortsFromFile();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading resorts: " + e.getMessage());
        }
    }

    public void addMockData() {
        addResort(new Resort("Alpine Valley", "Switzerland"));
        addResort(new Resort("Snowy Peaks", "Austria"));
        addResort(new Resort("Frosty Hills", "France"));
    }

    /**
     * Adds a resort and saves the updated set to file.
     */
    public void addResort(Resort resort) {
        resorts.add(resort);
        try {
            saveResortsToFile();
        } catch (IOException e) {
            System.err.println("Error saving resorts: " + e.getMessage());
        }
    }

    /**
     * Returns all resorts.
     */
    public Set<Resort> getResorts() {
        return resorts;
    }

    /**
     * Saves the set of resorts to a file.
     */
    public void saveResortsToFile() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(resorts);
        }
    }

    /**
     * Loads the set of resorts from a file if it exists.
     */
    public void loadResortsFromFile() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            resorts = (Set<Resort>) ois.readObject();
        }
    }
}