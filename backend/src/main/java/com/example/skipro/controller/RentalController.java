package com.example.skipro.controller;

import com.example.skipro.dto.RentalDto;
import com.example.skipro.model.Client;
import com.example.skipro.model.Equipment;
import com.example.skipro.model.Rental;
import com.example.skipro.service.ClientService;
import com.example.skipro.service.EquipmentService;
import com.example.skipro.service.RentalService;
import com.example.skipro.util.DtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Endpoints for creating and managing rentals.
 * <p>
 * Rental creation requires a {@code rentalClerkId}, which creates an explicit persisted link
 * Rental -> RentalClerk ("who processed the rental").
 * </p>
 */
@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final ClientService clientService;
    private final EquipmentService equipmentService;

    public RentalController(RentalService rentalService, ClientService clientService, EquipmentService equipmentService) {
        this.rentalService = rentalService;
        this.clientService = clientService;
        this.equipmentService = equipmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> create(
            @RequestParam String clientId,
            @RequestParam String equipmentId,
            @RequestParam String rentalClerkId
    ) {
        UUID cId;
        UUID eId;
        UUID rcId;
        try {
            cId = UUID.fromString(clientId);
            eId = UUID.fromString(equipmentId);
            rcId = UUID.fromString(rentalClerkId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        Client client = clientService.getClientById(cId);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        Equipment equipment = equipmentService.getEquipmentById(eId);
        if (equipment == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Rental rental = rentalService.rentEquipment(client, equipment, rcId);
            return ResponseEntity.ok(rental.getId());
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Void> returnRental(@PathVariable String id) {
        UUID rentalId;
        try {
            rentalId = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        Rental rental = rentalService.getRentalById(rentalId);
        if (rental == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            rentalService.returnEquipment(rental);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<RentalDto> getAll() {
        return rentalService.getAllRentals().stream()
                .map(DtoMapper::toRentalDto)
                .toList();
    }
}
