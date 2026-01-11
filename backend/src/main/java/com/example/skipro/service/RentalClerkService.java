package com.example.skipro.service;

import com.example.skipro.model.RentalClerk;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service responsible for managing rental clerks.
 * NOTE: currently in-memory. If rental clerks are required to be persisted, migrate to JPA.
 */
@Service
public class RentalClerkService {
    private final List<RentalClerk> allRentalClerks = new ArrayList<>();

    /**
     * Adds a rental clerk to the system.
     *
     * @param clerk the clerk to add
     */
    public void addRentalClerk(RentalClerk clerk) {
        allRentalClerks.add(clerk);
    }

    /**
     * Returns an unmodifiable list of all rental clerks.
     *
     * @return list of rental clerks
     */
    public List<RentalClerk> getAllRentalClerks() {
        return Collections.unmodifiableList(allRentalClerks);
    }

}