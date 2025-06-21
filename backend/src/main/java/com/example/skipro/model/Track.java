package com.example.skipro.model;

import com.example.skipro.model.enums.TrackDifficulty;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Track {
    private final UUID id = UUID.randomUUID();
    private final String name;
    private final TrackDifficulty difficulty;
    private final double lenghtKm;  // in kilometers
    private final Resort resort;

    private final Set<RescueTeam> rescueTeams = new HashSet<>();

    public Track(String name, TrackDifficulty difficulty, double lenghtKm, Resort resort) {
        if (resort == null) {
            throw new IllegalArgumentException("Truck must be associated with a resort.");
        }
        if (lenghtKm <= 0.1 || lenghtKm > 10) {
            throw new IllegalArgumentException("Track length must be between 0.1 and 10 kilometers.");
        }
        this.name = name;
        this.difficulty = difficulty;
        this.lenghtKm = lenghtKm;
        this.resort = resort;
        resort.addTrack(this);
    }

    public void addRescueTeam(RescueTeam team) {
        rescueTeams.add(team);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TrackDifficulty getDifficulty() {
        return difficulty;
    }

    public double getLenghtKm() {
        return lenghtKm;
    }

    public Resort getResort() {
        return resort;
    }

    public Set<RescueTeam> getRescueTeams() {
        return Collections.unmodifiableSet(rescueTeams);
    }

    @Override
    public String toString() {
        return name + " [" + difficulty + ", " + lenghtKm + "km] in " + resort.getName();
    }
}
