package com.example.skipro.service;

import com.example.skipro.model.RentalClerk;
import com.example.skipro.repository.RentalClerkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service responsible for managing rental clerks.
 */
@Service
public class RentalClerkService {
    private final RentalClerkRepository rentalClerkRepository;

    public RentalClerkService(RentalClerkRepository rentalClerkRepository) {
        this.rentalClerkRepository = rentalClerkRepository;
    }

    @Transactional
    public RentalClerk addRentalClerk(RentalClerk clerk) {
        if (clerk == null) {
            throw new IllegalArgumentException("Clerk cannot be null");
        }
        return rentalClerkRepository.save(clerk);
    }

    @Transactional(readOnly = true)
    public List<RentalClerk> getAllRentalClerks() {
        return rentalClerkRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RentalClerk getRentalClerkById(UUID id) {
        return id == null ? null : rentalClerkRepository.findById(id).orElse(null);
    }

    /**
     * Marks that the clerk handled one more rental.
     */
    @Transactional
    public RentalClerk incrementRentalsHandled(UUID clerkId) {
        RentalClerk clerk = rentalClerkRepository.findById(clerkId)
                .orElseThrow(() -> new IllegalArgumentException("RentalClerk not found: " + clerkId));
        clerk.incrementRentalsHandled();
        return rentalClerkRepository.save(clerk);
    }
}