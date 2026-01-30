package com.example.skipro.model;

import com.example.skipro.model.enums.TrackDifficulty;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.*;

/**
 * Represents a ski resort that contains multiple {@link Track}s and employs staff through {@link Employment} contracts.
 * <p>
 * A {@code Resort} aggregates operational information such as opening and closing hours and maintains two
 * bidirectional associations:
 * <ul>
 *   <li>{@link #employments} – links to every employment contract involving the resort.</li>
 *   <li>{@link #tracks} – the set of ski/snowboard tracks available at the resort.</li>
 * </ul>
 * Each resort is uniquely identified by an immutable {@link UUID} generated at construction time.
 * </p>
 */
@Entity
@Table(name = "resorts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_resort_name", columnNames = {"name"})
        }
)
public class Resort {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String location;

    private final LocalTime openingHour = LocalTime.of(8, 0);
    private final LocalTime closingHour = LocalTime.of(17, 0);

    @OneToMany(mappedBy = "resort", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Employment> employments = new HashSet<>();

    @OneToMany(mappedBy = "resort", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Track> tracks = new HashSet<>();

    protected Resort() {
        // for JPA
    }

    /**
     * Constructs a {@code Resort} with the given name and location.
     *
     * @param name     resort name
     * @param location geographic location or address
     */
    public Resort(String name, String location) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Resort name cannot be null/blank");
        }
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Resort location cannot be null/blank");
        }
        this.name = name;
        this.location = location;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Set<Employment> getEmployments() {
        return Collections.unmodifiableSet(employments);
    }

    void addEmployment(Employment e) {
        if (e == null) return;
        employments.add(e);
        e.setResort(this);
    }

    void addTrack(Track track) {
        tracks.add(track);
    }

    public Track createTrack(String name, TrackDifficulty difficulty, double lengthKm) {
        Track track = new Track(name, difficulty, lengthKm, this);
        tracks.add(track);
        return track;
    }

    public Set<Track> getTracks() {
        return Collections.unmodifiableSet(tracks);
    }

    @Override
    public String toString() {
        return name + " (" + location + ")";
    }
}
