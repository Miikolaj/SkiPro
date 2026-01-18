package com.example.skipro.service;

import com.example.skipro.model.RescueTeam;
import com.example.skipro.model.RescueWorker;
import com.example.skipro.model.RescueWorkerTeamAssignment;
import com.example.skipro.repository.RescueTeamRepository;
import com.example.skipro.repository.RescueWorkerRepository;
import com.example.skipro.repository.RescueWorkerTeamAssignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing the assignment history of RescueWorkers to RescueTeams.
 * Implements CLD requirement: worker can be transferred; history is preserved.
 */
@Service
public class RescueWorkerTeamAssignmentService {

    private final RescueWorkerTeamAssignmentRepository assignmentRepository;
    private final RescueWorkerRepository rescueWorkerRepository;
    private final RescueTeamRepository rescueTeamRepository;

    public RescueWorkerTeamAssignmentService(
            RescueWorkerTeamAssignmentRepository assignmentRepository,
            RescueWorkerRepository rescueWorkerRepository,
            RescueTeamRepository rescueTeamRepository
    ) {
        this.assignmentRepository = assignmentRepository;
        this.rescueWorkerRepository = rescueWorkerRepository;
        this.rescueTeamRepository = rescueTeamRepository;
    }

    /**
     * Assigns a worker to a team starting at {@code startDate}.
     * If worker currently has an active assignment, it is ended the day before {@code startDate}.
     */
    @Transactional
    public RescueWorkerTeamAssignment assign(UUID workerId, UUID teamId, LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("startDate must not be null");
        }

        RescueWorker worker = rescueWorkerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("RescueWorker not found: " + workerId));
        RescueTeam team = rescueTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("RescueTeam not found: " + teamId));

        assignmentRepository.findFirstByRescueWorkerIdAndEndDateIsNull(workerId)
                .ifPresent(active -> {
                    // End previous assignment on startDate-1 (inclusive assignment ranges)
                    LocalDate end = startDate.minusDays(1);
                    if (end.isBefore(active.getStartDate())) {
                        throw new IllegalArgumentException("New startDate overlaps existing assignment.");
                    }
                    active.end(end);
                    assignmentRepository.save(active);
                });

        RescueWorkerTeamAssignment newAssignment = new RescueWorkerTeamAssignment(worker, team, startDate);
        return assignmentRepository.save(newAssignment);
    }

    @Transactional(readOnly = true)
    public List<RescueWorkerTeamAssignment> getHistoryForWorker(UUID workerId) {
        return assignmentRepository.findByRescueWorkerIdOrderByStartDateDesc(workerId);
    }

    @Transactional(readOnly = true)
    public RescueWorkerTeamAssignment getActiveAssignment(UUID workerId) {
        return assignmentRepository.findFirstByRescueWorkerIdAndEndDateIsNull(workerId).orElse(null);
    }
}

