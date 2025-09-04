package com.example.skipro.service;

import com.example.skipro.model.RescueWorker;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing rescue workers and persisting them to a file.
 */
@Service
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
        load();
    }

    /**
     * Saves the list of rescue workers to a file.
     *
     * @throws IOException if an I/O error occurs during writing
     */
    public void saveRescueWorkersToFile()  {
        File file = new File(FILE_NAME);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(rescueWorkers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the list of rescue workers from a file if it exists.
     *
     * @throws IOException            if an I/O error occurs during reading
     * @throws ClassNotFoundException if the file does not contain a valid List<RescueWorker>
     */
    public void load() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(rescueWorkers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
