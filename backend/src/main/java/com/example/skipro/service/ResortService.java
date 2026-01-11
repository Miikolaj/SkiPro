package com.example.skipro.service;

import com.example.skipro.model.Resort;
import com.example.skipro.repository.ResortRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service responsible for managing resorts.
 */
@Service
public class ResortService {
    private final ResortRepository resortRepository;

    public ResortService(ResortRepository resortRepository) {
        this.resortRepository = resortRepository;
    }

    /**
     * Adds a resort and saves it to the database.
     */
    public void addResort(Resort resort) {
        resortRepository.save(resort);
    }

    /**
     * Returns a list of all resorts from the database.
     */
    public List<Resort> getAllResorts() {
        return resortRepository.findAll();
    }

    /**
     * Returns a resort by its ID.
     */
    public Resort getResortById(UUID id) {
        return id == null ? null : resortRepository.findById(id).orElse(null);
    }
}