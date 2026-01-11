package com.example.skipro.model;

import com.example.skipro.model.enums.TrackDifficulty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a ski or snowboard track (run) within a {@link Resort}.
 * <p>
 * Each {@code Track} is uniquely identified by an immutable {@link UUID}, has a human-readable
 * {@link #name}, a {@link #difficulty} rating, and a measurable {@link #lenghtKm}. A track is
 * permanently associated with exactly one resort and can have zero or more {@link RescueTeam}s
 * assigned to it.
 * </p>
 */
@Entity
@Table(name = "tracks")
public class Track implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TrackDifficulty difficulty;

    private double lenghtKm;  // in kilometers

    @ManyToOne(optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private Resort resort;

    @Transient
    private final Set<RescueTeam> rescueTeams = new HashSet<>();

    protected Track() {
        // for JPA
    }

    /**
     * Constructs a {@code Track} and registers it with the given resort.
     *
     * @param name       track name
     * @param difficulty difficulty rating
     * @param lenghtKm   length in kilometres (must be &gt; 0.1 and ≤ 10)
     * @param resort     owning resort (non-null)
     * @throws IllegalArgumentException if {@code resort} is {@code null} or {@code lengthKm} is out of range
     */
    Track(String name, TrackDifficulty difficulty, double lenghtKm, Resort resort) {
        if (resort == null) {
            throw new IllegalArgumentException("Track must be associated with a resort.");
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
