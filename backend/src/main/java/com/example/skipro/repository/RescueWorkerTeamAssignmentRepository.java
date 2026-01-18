package com.example.skipro.repository;

import com.example.skipro.model.RescueWorkerTeamAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RescueWorkerTeamAssignmentRepository extends JpaRepository<RescueWorkerTeamAssignment, UUID> {
    List<RescueWorkerTeamAssignment> findByRescueWorkerIdOrderByStartDateDesc(UUID rescueWorkerId);

    Optional<RescueWorkerTeamAssignment> findFirstByRescueWorkerIdAndEndDateIsNull(UUID rescueWorkerId);
}

