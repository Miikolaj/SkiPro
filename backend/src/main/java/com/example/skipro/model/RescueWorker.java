package com.example.skipro.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class RescueWorker extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String licenseNumber;
    private final List<String> qualifications;
    private RescueTeam rescueTeam;

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
