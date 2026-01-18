package com.example.skipro.controller;

import com.example.skipro.model.Resort;
import com.example.skipro.service.ResortService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Basic REST endpoints for Resorts.
 */
@RestController
@RequestMapping("/resorts")
public class ResortController {

    private final ResortService resortService;

    public ResortController(ResortService resortService) {
        this.resortService = resortService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> create(@RequestParam String name, @RequestParam String location) {
        if (name == null || name.isBlank() || location == null || location.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Resort resort = new Resort(name, location);
        resortService.addResort(resort);
        return ResponseEntity.ok(resort.getId());
    }

    @GetMapping
    public List<Resort> getAll() {
        return resortService.getAllResorts();
    }
}

