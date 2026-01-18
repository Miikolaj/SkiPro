package com.example.skipro.controller;

import com.example.skipro.model.RescueTeam;
import com.example.skipro.service.RescueTeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Basic REST endpoints for Rescue Teams.
 */
@RestController
@RequestMapping("/rescue-teams")
public class RescueTeamController {

    private final RescueTeamService rescueTeamService;

    public RescueTeamController(RescueTeamService rescueTeamService) {
        this.rescueTeamService = rescueTeamService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> create(
            @RequestParam String name,
            @RequestParam String trackId
    ) {
        final UUID tId;
        try {
            tId = UUID.fromString(trackId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        RescueTeam team;
        try {
            team = rescueTeamService.createTeam(name, tId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(team.getId());
    }

    @GetMapping
    public List<RescueTeam> getAll() {
        return rescueTeamService.getAllTeams();
    }

    @GetMapping("/track/{trackId}")
    public ResponseEntity<List<RescueTeam>> getForTrack(@PathVariable String trackId) {
        try {
            return ResponseEntity.ok(rescueTeamService.getTeamsForTrack(UUID.fromString(trackId)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}

