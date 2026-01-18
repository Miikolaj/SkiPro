package com.example.skipro.controller;

import com.example.skipro.model.Resort;
import com.example.skipro.model.Track;
import com.example.skipro.model.enums.TrackDifficulty;
import com.example.skipro.service.ResortService;
import com.example.skipro.service.TrackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

/**
 * Basic REST endpoints for Tracks.
 */
@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;
    private final ResortService resortService;

    public TrackController(TrackService trackService, ResortService resortService) {
        this.trackService = trackService;
        this.resortService = resortService;
    }

    /**
     * Creates a Track within a Resort using the domain factory method (composition-like constraint).
     */
    @PostMapping("/create")
    public ResponseEntity<UUID> create(
            @RequestParam String resortId,
            @RequestParam String name,
            @RequestParam String difficulty,
            @RequestParam double lengthKm
    ) {
        final UUID rId;
        try {
            rId = UUID.fromString(resortId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        Resort resort = resortService.getResortById(rId);
        if (resort == null) {
            return ResponseEntity.notFound().build();
        }

        final TrackDifficulty diff;
        try {
            diff = TrackDifficulty.valueOf(difficulty);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

        final Track track;
        try {
            track = resort.createTrack(name, diff, lengthKm);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

        trackService.addTrack(track);
        return ResponseEntity.ok(track.getId());
    }

    @GetMapping
    public Set<Track> getAll() {
        return trackService.getTracks();
    }
}

