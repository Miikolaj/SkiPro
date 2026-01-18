package com.example.skipro.controller;

import com.example.skipro.dto.EquipmentDto;
import com.example.skipro.model.Equipment;
import com.example.skipro.service.EquipmentService;
import com.example.skipro.util.DtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Basic REST endpoints for Equipment.
 */
@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestParam String name,
            @RequestParam String size,
            @RequestParam int cost
    ) {
        if (name == null || name.isBlank() || size == null || size.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (cost < 0) {
            return ResponseEntity.badRequest().build();
        }

        Equipment equipment = new Equipment(name, size, cost);
        equipmentService.addEquipment(equipment);
        return ResponseEntity.ok(equipment.getId());
    }

    @GetMapping
    public List<EquipmentDto> getAll() {
        return equipmentService.getAllEquipment().stream()
                .map(DtoMapper::toEquipmentDto)
                .toList();
    }
}
