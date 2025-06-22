package com.example.skipro.service;

import com.example.skipro.model.Equipment;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service responsible for managing equipment and persisting it to a file.
 */
public class EquipmentService {
    private final List<Equipment> equipmentList = new ArrayList<>(); // List containing all equipment.
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/equipment.ser"; // Name of the file used for saving equipment.

    /**
     * Constructs an EquipmentService and loads equipment from file.
     */
    public EquipmentService() {
        try {
            loadEquipmentFromFile();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading equipment: " + e.getMessage());
        }
    }

    /**
     * Adds a piece of equipment to the registry and persists the change.
     *
     * @param equipment the equipment to add
     * @throws IOException if an I/O error occurs during writing
     */
    public void addEquipment(Equipment equipment) throws IOException {
        equipmentList.add(equipment);
        saveEquipmentToFile();
    }

    /**
     * Returns an unmodifiable view of all equipment.
     *
     * @return list of equipment
     */
    public List<Equipment> getAllEquipment() {
        return Collections.unmodifiableList(equipmentList);
    }

    /**
     * Saves the current equipment list to a file.
     *
     * @throws IOException if an I/O error occurs during writing
     */
    public void saveEquipmentToFile() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(equipmentList);
        }
    }

    /**
     * Loads equipment from the persistent file into memory. If the file does not exist, the method returns silently.
     *
     * @throws IOException            if an I/O error occurs during reading
     * @throws ClassNotFoundException if the file does not contain a valid List<Equipment>
     */
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