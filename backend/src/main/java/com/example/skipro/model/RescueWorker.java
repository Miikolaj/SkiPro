package com.example.skipro.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a certified rescue worker responsible for on-slope first-aid and evacuation duties.
 * <p>
 * A {@code RescueWorker} extends {@link Employee} by introducing a unique professional
 * {@link #licenseNumber}, a list of specialised {@link #qualifications} (e.g., “Avalanche II”,
 * “EMT-B”), and an association to a {@link RescueTeam}. Salary is calculated based on a base wage
 * plus a bonus per qualification held.
 * </p>
 */
public class RescueWorker extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String licenseNumber;
    private final List<String> qualifications;
    private RescueTeam rescueTeam;

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
        this.qualifications = List.copyOf(qualifications);
    }

    void setRescueTeam(RescueTeam team) {
        this.rescueTeam = team;
    }

    public RescueTeam getRescueTeam() {
        return rescueTeam;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public List<String> getQualifications() {
        return qualifications;
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
