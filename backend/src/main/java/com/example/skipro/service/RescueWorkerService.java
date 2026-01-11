package com.example.skipro.service;

import com.example.skipro.model.RescueWorker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service responsible for managing rescue workers.
 * NOTE: currently in-memory. If rescue workers are required to be persisted, migrate to JPA.
 */
@Service
public class RescueWorkerService {
    private final List<RescueWorker> rescueWorkers = new ArrayList<>();

    public void addRescueWorker(RescueWorker worker) {
        rescueWorkers.add(worker);
    }

    public List<RescueWorker> getAllRescueWorkers() {
        return Collections.unmodifiableList(rescueWorkers);
    }
}
