package model;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Resort {
    private final UUID id = UUID.randomUUID();
    private final String name;
    private final String location;
    private final LocalTime openingHour = LocalTime.of(8, 0);
    private final LocalTime closingHour = LocalTime.of(17, 0);

    private final Set<Employment> employments = new HashSet<>();
    private final Set<Track> tracks = new HashSet<>();

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
