package com.example.skipro.service;

import com.example.skipro.model.Resort;
import com.example.skipro.util.PersistenceManager;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Service responsible for managing resorts and persisting them to a file.
 */
@Service
public class ResortService {
    /**
     * Set containing all resorts.
     */
    private Set<Resort> resorts = new HashSet<>();
    private final PersistenceManager<Resort> persistence = new PersistenceManager<>("src/main/java/com/example/skipro/data/resorts.ser");

    /**
     * Constructs a ResortService and loads resorts from file.
     */
    public ResortService() {
        resorts = persistence.loadSet();
    }

    /**
     * Adds a resort and saves the updated set to file.
     */
    public void addResort(Resort resort) {
        resorts.add(resort);
        persistence.saveSet(resorts);
    }
}