package com.example.skipro.service;

import com.example.skipro.model.Instructor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing instructors and persisting them to a file.
 */
public class InstructorService {
    private static final String INSTRUCTORS_FILE = "src/main/java/com/example/skipro/data/instructors.ser"; //Name of the file used for saving instructors.
    private final List<Instructor> instructorRegistry = new ArrayList<>(); // Registry containing all instructors.

    /**
     * Constructs an InstructorService and loads instructors from file.
     */
    public InstructorService() {
        loadInstructors();
    }

    /**
     * Returns a copyof the list of all instructors.
     *
     * @return list of instructors
     */
    public List<Instructor> getAllInstructors() {
        return new ArrayList<>(instructorRegistry);
    }

    /**
     * Retrieves an instructor by identifier.
     *
     * @param id the instructor identifier
     * @return the instructor if found, otherwise {@code null}
     */
    public Instructor getInstructorById(String id) {
        return instructorRegistry.stream()
                .filter(i -> String.valueOf(i.getId()).equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Saves the current instructor registry to a file. Necessary directories are created automatically.
     */
    public void saveInstructors() {
        File file = new File(INSTRUCTORS_FILE);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(instructorRegistry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads instructors from the persistent file into memory. If the file does not exist, the method returns silently.
     */
    public void loadInstructors() {
        File file = new File(INSTRUCTORS_FILE);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Instructor> loaded = (List<Instructor>) ois.readObject();
            instructorRegistry.clear();
            instructorRegistry.addAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}