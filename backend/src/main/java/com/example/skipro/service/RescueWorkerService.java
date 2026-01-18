package com.example.skipro.service;

import com.example.skipro.model.RescueWorker;
import com.example.skipro.repository.RescueWorkerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service responsible for managing rescue workers.
 */
@Service
public class RescueWorkerService {
    private final RescueWorkerRepository rescueWorkerRepository;

    public RescueWorkerService(RescueWorkerRepository rescueWorkerRepository) {
        this.rescueWorkerRepository = rescueWorkerRepository;
    }

    @Transactional
    public RescueWorker addRescueWorker(RescueWorker worker) {
        if (worker == null) {
            throw new IllegalArgumentException("Worker cannot be null");
        }
        rescueWorkerRepository.findByLicenseNumber(worker.getLicenseNumber())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("License number already exists: " + worker.getLicenseNumber());
                });

        return rescueWorkerRepository.save(worker);
    }

    @Transactional(readOnly = true)
    public List<RescueWorker> getAllRescueWorkers() {
        return rescueWorkerRepository.findAll();
    }
}
