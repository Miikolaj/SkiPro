package com.example.skipro.controller;

import com.example.skipro.model.RescueWorker;
import com.example.skipro.service.RescueWorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Basic REST endpoints for Rescue Workers.
 */
@RestController
@RequestMapping("/rescue-workers")
public class RescueWorkerController {

    private final RescueWorkerService rescueWorkerService;

    public RescueWorkerController(RescueWorkerService rescueWorkerService) {
        this.rescueWorkerService = rescueWorkerService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> create(
            @RequestParam int yearsOfExperience,
            @RequestParam String birthDate,
            @RequestParam String lastName,
            @RequestParam String firstName,
            @RequestParam String licenseNumber,
            @RequestParam List<String> qualifications
    ) {
        RescueWorker worker;
        try {
            worker = new RescueWorker(
                    yearsOfExperience,
                    LocalDate.parse(birthDate),
                    lastName,
                    firstName,
                    licenseNumber,
                    qualifications
            );
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

        try {
            RescueWorker saved = rescueWorkerService.addRescueWorker(worker);
            return ResponseEntity.ok(saved.getId());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<RescueWorker> getAll() {
        return rescueWorkerService.getAllRescueWorkers();
    }
}

