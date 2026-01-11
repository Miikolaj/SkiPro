package com.example.skipro.service;

import com.example.skipro.model.Equipment;
import com.example.skipro.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service responsible for managing equipment and persisting it to a file.
 */
@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    /**
     * Adds a piece of equipment to the registry and persists the change.
     *
     * @param equipment the equipment to add
     */
    public void addEquipment(Equipment equipment) {
        equipmentRepository.save(equipment);
    }

    /**
     * Returns all equipment.
     *
     * @return list of equipment
     */
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    /**
     * Returns the equipment with the given id.
     *
     * @param id the id of the equipment to retrieve
     * @return the equipment with the given id, or null if no such equipment exists
     */
    public Equipment getEquipmentById(UUID id) {
        return id == null ? null : equipmentRepository.findById(id).orElse(null);
    }
}