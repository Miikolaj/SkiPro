package com.example.skipro.service;

import com.example.skipro.model.Equipment;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Service responsible for managing equipment and serialization.
 */
public class EquipmentService {
    private final List<Equipment> equipmentList = new ArrayList<>();
    private static final String FILE_NAME = "/data/equipment.ser";

    public EquipmentService() {
        try {
            loadEquipmentFromFile();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading equipment: " + e.getMessage());
        }
    }

    public void addEquipment(Equipment equipment) throws IOException {
        equipmentList.add(equipment);
        saveEquipmentToFile();
    }

    public List<Equipment> getAllEquipment() {
        return Collections.unmodifiableList(equipmentList);
    }

    public void saveEquipmentToFile() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(equipmentList);
        }
    }

    public void loadEquipmentFromFile() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            List<Equipment> loadedList = (List<Equipment>) ois.readObject();
            equipmentList.clear();
            equipmentList.addAll(loadedList);
        }
    }
}