package com.example.skipro.controller;

import com.example.skipro.model.Employment;
import com.example.skipro.service.EmploymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Endpoints for managing employments (association-class Resort <-> Employee).
 */
@RestController
@RequestMapping("/employments")
public class EmploymentController {

    private final EmploymentService employmentService;

    public EmploymentController(EmploymentService employmentService) {
        this.employmentService = employmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> create(
            @RequestParam String resortId,
            @RequestParam String employeeId,
            @RequestParam String startDate
    ) {
        UUID rId;
        UUID eId;
        try {
            rId = UUID.fromString(resortId);
            eId = UUID.fromString(employeeId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        Employment employment;
        try {
            employment = employmentService.addEmployment(rId, eId, LocalDate.parse(startDate));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(employment.getId());
    }

    @GetMapping
    public List<Employment> getAll() {
        return employmentService.getAllEmployments();
    }

    @GetMapping("/resort/{id}")
    public ResponseEntity<List<Employment>> getForResort(@PathVariable String id) {
        try {
            return ResponseEntity.ok(employmentService.getEmploymentsForResort(UUID.fromString(id)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<List<Employment>> getForEmployee(@PathVariable String id) {
        try {
            return ResponseEntity.ok(employmentService.getEmploymentsForEmployee(UUID.fromString(id)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}

