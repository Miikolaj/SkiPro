package com.example.skipro.service;

import com.example.skipro.model.Instructor;
import com.example.skipro.util.PersistenceManager;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing instructors and persisting them to a file.
 */
@Service
public class InstructorService {
    private final PersistenceManager<Instructor> persistence = new PersistenceManager<>("src/main/java/com/example/skipro/data/instructors.ser"); //Name of the file used for saving instructors.
    private List<Instructor> instructorRegistry = new ArrayList<>(); // Registry containing all instructors.

    /**
     * Constructs an InstructorService and loads instructors from file.
     */
    public InstructorService() {
       instructorRegistry = persistence.load();
    }

    /**
     * Adds a new instructor to the registry and persists the change to file.
     *
     * @param instructor the instructor to add
     */
    public void addInstructor(Instructor instructor) {
        instructorRegistry.add(instructor);
        persistence.save(instructorRegistry);
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
}