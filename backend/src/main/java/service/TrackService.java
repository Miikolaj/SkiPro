package service;

import model.Resort;
import model.Track;
import model.enums.TrackDifficulty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TrackService {
    private final List<Track> tracks = new ArrayList<>();

    public Track createTrack(String name, TrackDifficulty difficulty, double lengthKm, Resort resort) {
        Track track = new Track(name, difficulty, lengthKm, resort);
        tracks.add(track);
        return track;
    }

    public List<Track> getAllTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public List<Track> getTracksByResort(Resort resort) {
        return tracks.stream()
                .filter(track -> track.getResort().equals(resort))
                .toList();
    }

    public Optional<Track> findTrackByName(String name) {
        return tracks.stream()
                .filter(track -> track.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
