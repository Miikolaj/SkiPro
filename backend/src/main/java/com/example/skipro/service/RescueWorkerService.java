package com.example.skipro.service;

import com.example.skipro.model.RescueWorker;
import com.example.skipro.util.PersistenceManager;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing rescue workers and persisting them to a file.
 */
@Service
public class RescueWorkerService {
    private final PersistenceManager<RescueWorker> persistence = new PersistenceManager<>("src/main/java/com/example/skipro/data/rescue_workers.ser");
    private List<RescueWorker> rescueWorkers = new ArrayList<>(); //List containing all rescue workers.

    /**
     * Constructs a RescueWorkerService and loads rescue workers from file.
     *
     * @throws IOException            if an I/O error occurs during reading
     * @throws ClassNotFoundException if the file does not contain a valid List<RescueWorker>
     */
    public RescueWorkerService() {
       rescueWorkers = persistence.load();
    }
}
