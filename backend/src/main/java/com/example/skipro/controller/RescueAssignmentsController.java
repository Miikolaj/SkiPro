package com.example.skipro.controller;

import com.example.skipro.model.RescueWorkerTeamAssignment;
import com.example.skipro.service.RescueWorkerTeamAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Endpoints for RescueWorker <-> RescueTeam assignments (history-preserving).
 */
@RestController
@RequestMapping("/rescue-assignments")
public class RescueAssignmentsController {

    private final RescueWorkerTeamAssignmentService assignmentService;

    public RescueAssignmentsController(RescueWorkerTeamAssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/assign")
    public ResponseEntity<UUID> assign(
            @RequestParam String workerId,
            @RequestParam String teamId,
            @RequestParam String startDate
    ) {
        UUID wId;
        UUID tId;
        try {
            wId = UUID.fromString(workerId);
            tId = UUID.fromString(teamId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        RescueWorkerTeamAssignment assignment;
        try {
            assignment = assignmentService.assign(wId, tId, LocalDate.parse(startDate));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(assignment.getId());
    }

    @GetMapping("/worker/{id}")
    public ResponseEntity<List<RescueWorkerTeamAssignment>> history(@PathVariable String id) {
        try {
            return ResponseEntity.ok(assignmentService.getHistoryForWorker(UUID.fromString(id)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/worker/{id}/active")
    public ResponseEntity<RescueWorkerTeamAssignment> active(@PathVariable String id) {
        try {
            return ResponseEntity.ok(assignmentService.getActiveAssignment(UUID.fromString(id)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}

