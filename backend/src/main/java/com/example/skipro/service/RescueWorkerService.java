package com.example.skipro.service;

import com.example.skipro.model.RescueWorker;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RescueWorkerService {
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/rescue_workers.ser";
    private final List<RescueWorker> rescueWorkers = new ArrayList<>();

    public RescueWorkerService() throws IOException, ClassNotFoundException {
        loadRescueWorkersFromFile();
    }

    public void saveRescueWorkersToFile() throws IOException {
        File file = new File(FILE_NAME);
        file.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(rescueWorkers);
        }
    }

    public void loadRescueWorkersFromFile() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (!file.exists() || file.length() == 0) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<RescueWorker> loadedList = (List<RescueWorker>) ois.readObject();
            rescueWorkers.clear();
            rescueWorkers.addAll(loadedList);
        }
    }
}
