package com.example.skipro.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Association-class representing an assignment of a {@link RescueWorker} to a {@link RescueTeam}.
 * <p>
 * Implements the project requirement from CLD point 12: a worker can be transferred between teams and
 * the assignment history is preserved.
 * </p>
 */
@Entity
@Table(name = "rescue_worker_team_assignments")
public class RescueWorkerTeamAssignment {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rescue_worker_id", nullable = false)
    private RescueWorker rescueWorker;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rescue_team_id", nullable = false)
    private RescueTeam rescueTeam;

    /** Assignment start date (inclusive). */
    @Column(nullable = false)
    private LocalDate startDate;

    /** Assignment end date (inclusive); null means the assignment is currently active. */
    private LocalDate endDate;

    protected RescueWorkerTeamAssignment() {
        // for JPA
    }

    public RescueWorkerTeamAssignment(RescueWorker rescueWorker, RescueTeam rescueTeam, LocalDate startDate) {
        if (rescueWorker == null || rescueTeam == null || startDate == null) {
            throw new IllegalArgumentException("rescueWorker, rescueTeam and startDate must not be null");
        }
        this.rescueWorker = rescueWorker;
        this.rescueTeam = rescueTeam;
        this.startDate = startDate;
    }

    public UUID getId() {
        return id;
    }

    public RescueWorker getRescueWorker() {
        return rescueWorker;
    }

    public RescueTeam getRescueTeam() {
        return rescueTeam;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return endDate == null;
    }

    public void end(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("endDate cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate cannot be before startDate");
        }
        this.endDate = endDate;
    }
}

