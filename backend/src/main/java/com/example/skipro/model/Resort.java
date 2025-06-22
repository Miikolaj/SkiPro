package com.example.skipro.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
public class Resort implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id = UUID.randomUUID();
    private final String name;
    private final String location;
    private final LocalTime openingHour = LocalTime.of(8, 0);
    private final LocalTime closingHour = LocalTime.of(17, 0);

    private final Set<Employment> employments = new HashSet<>();
    private final Set<Track> tracks = new HashSet<>();

    /**
     * Constructs a {@code Resort} with the given name and location.
     *
     * @param name     resort name
     * @param location geographic location or address
     */
    public Resort(String name, String location) {
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
        employments.add(e);
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }

    public Set<Track> getTracks() {
        return Collections.unmodifiableSet(tracks);
    }

    @Override
    public String toString() {
        return name + " (" + location + ")";
    }
}
