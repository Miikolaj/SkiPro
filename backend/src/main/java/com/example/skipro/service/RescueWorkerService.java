package com.example.skipro.service;

import com.example.skipro.model.RescueWorker;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing rescue workers and persisting them to a file.
 */
public class RescueWorkerService {
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/rescue_workers.ser"; //Name of the file used for saving rescue workers.
    private final List<RescueWorker> rescueWorkers = new ArrayList<>(); //List containing all rescue workers.

    /**
     * Constructs a RescueWorkerService and loads rescue workers from file.
     *
     * @throws IOException            if an I/O error occurs during reading
     * @throws ClassNotFoundException if the file does not contain a valid List<RescueWorker>
     */
    public RescueWorkerService() throws IOException, ClassNotFoundException {
        loadRescueWorkersFromFile();
    }

    /**
     * Saves the list of rescue workers to a file.
     *
     * @throws IOException if an I/O error occurs during writing
     */
    public void saveRescueWorkersToFile() throws IOException {
        File file = new File(FILE_NAME);
        file.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(rescueWorkers);
        }
    }

    /**
     * Loads the list of rescue workers from a file if it exists.
     *
     * @throws IOException            if an I/O error occurs during reading
     * @throws ClassNotFoundException if the file does not contain a valid List<RescueWorker>
     */
    public void loadRescueWorkersFromFile() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        if (!file.exists() || file.length() == 0) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<RescueWorker> loadedList = (List<RescueWorker>) ois.readObject();
            rescueWorkers.clear();
            rescueWorkers.addAll(loadedList);
        }
    }
}
