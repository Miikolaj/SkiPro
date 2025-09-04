package com.example.skipro.service;

import com.example.skipro.model.RentalClerk;
import com.example.skipro.util.PersistenceManager;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing rental clerks and persisting them to a file.
 */
@Service
public class RentalClerkService {
    private final PersistenceManager<RentalClerk> persistence = new PersistenceManager<>("src/main/java/com/example/skipro/data/rentalclerks.ser"); // Name of the file used for saving rental clerks.
    private List<RentalClerk> allRentalClerks = new ArrayList<>();

    /**
     * Constructs a RentalClerkService and loads clerks from file.
     */
    public RentalClerkService() {
        allRentalClerks = persistence.load();
    }

    /**
     * Adds a rental clerk to the system and persists the change.
     *
     * @param clerk the clerk to add
     */
    public void addRentalClerk(RentalClerk clerk) {
        allRentalClerks.add(clerk);
        persistence.save(allRentalClerks);
    }

    /**
     * Returns a mutable list of all rental clerks.
     *
     * @return list of rental clerks
     */
    public List<RentalClerk> getAllRentalClerks() {
        return allRentalClerks;
    }

}