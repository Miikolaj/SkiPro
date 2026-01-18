package com.example.skipro.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a certified rescue worker responsible for on-slope first-aid and evacuation duties.
 */
@Entity
@Table(name = "rescue_workers")
public class RescueWorker extends Employee {

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @ElementCollection
    @CollectionTable(name = "rescue_worker_qualifications", joinColumns = @JoinColumn(name = "rescue_worker_id"))
    @Column(name = "qualification")
    private List<String> qualifications = new ArrayList<>();

    /**
     * Assignment history for this worker (point 12 requirement).
     */
    @OneToMany(mappedBy = "rescueWorker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RescueWorkerTeamAssignment> teamAssignments = new ArrayList<>();

    protected RescueWorker() {
        // for JPA
    }

    /**
     * Constructs a {@code RescueWorker} with the provided personal details and credentials.
     *
     * @param yearsOfExperience total professional experience in years
     * @param birthDate         date of birth
     * @param lastName          worker’s last name
     * @param firstName         worker’s first name
     * @param licenseNumber     unique licence/certification number
     * @param qualifications    non-empty list of qualification strings
     * @throws IllegalArgumentException if {@code qualifications} is {@code null} or empty
     */
    public RescueWorker(int yearsOfExperience, LocalDate birthDate, String lastName, String firstName, String licenseNumber, List<String> qualifications) {
        super(yearsOfExperience, birthDate, lastName, firstName);
        this.licenseNumber = licenseNumber;
        if (qualifications == null || qualifications.isEmpty()) {
            throw new IllegalArgumentException("Qualifications cannot be null or empty");
        }
        this.qualifications = new ArrayList<>(qualifications);
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public List<String> getQualifications() {
        return List.copyOf(qualifications);
    }

    public List<RescueWorkerTeamAssignment> getTeamAssignments() {
        return List.copyOf(teamAssignments);
    }

    /**
     * Convenience method: returns current team based on active assignment.
     */
    @Transient
    public RescueTeam getCurrentRescueTeam() {
        return teamAssignments.stream()
                .filter(RescueWorkerTeamAssignment::isActive)
                .map(RescueWorkerTeamAssignment::getRescueTeam)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getRole() {
        return "";
    }

    @Override
    public double calculateSalary() {
        return 3200.0 + qualifications.size() * 250.0;
    }
}
