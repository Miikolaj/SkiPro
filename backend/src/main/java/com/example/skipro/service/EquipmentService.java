package com.example.skipro.service;

import com.example.skipro.model.Equipment;
import com.example.skipro.util.PersistenceManager;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service responsible for managing equipment and persisting it to a file.
 */
@Service
public class EquipmentService {
    private List<Equipment> equipmentList = new ArrayList<>(); // List containing all equipment.
    private final PersistenceManager<Equipment> persistence = new PersistenceManager<>("src/main/java/com/example/skipro/data/equipment.ser"); // Name of the file used for saving equipment.

    /**
     * Constructs an EquipmentService and loads equipment from file.
     */
    public EquipmentService() {
        equipmentList = persistence.load();
    }

    /**
     * Adds a piece of equipment to the registry and persists the change.
     *
     * @param equipment the equipment to add
     * @throws IOException if an I/O error occurs during writing
     */
    public void addEquipment(Equipment equipment) throws IOException {
        equipmentList.add(equipment);
        persistence.save(equipmentList);
    }

    /**
     * Returns an unmodifiable view of all equipment.
     *
     * @return list of equipment
     */
    public List<Equipment> getAllEquipment() {
        return Collections.unmodifiableList(equipmentList);
    }

}