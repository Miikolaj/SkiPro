package service;

import model.Equipment;

import java.util.*;

public class EquipmentService {
    private final List<Equipment> equipmentList = new ArrayList<>();

    public Equipment addEquipment(String name, String size, int cost) {
        Equipment equipment = new Equipment(name, size, cost);
        equipmentList.add(equipment);
        return equipment;
    }

    public List<Equipment> getAllEquipment() {
        return Collections.unmodifiableList(equipmentList);
    }

    public List<Equipment> getAvailableEquipment() {
        return equipmentList.stream()
                .filter(e -> !e.isInUse())
                .toList();
    }

    public Optional<Equipment> getEquipmentById(UUID id) {
        return equipmentList.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }
}
