package com.example.skipro.service;

import com.example.skipro.model.Track;
import com.example.skipro.repository.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service responsible for managing ski tracks.
 */
@Service
public class TrackService {
    private final TrackRepository trackRepository;

    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    /**
     * Adds a track and saves it to the database.
     *
     * @param track the track to add
     */
    public void addTrack(Track track) {
        trackRepository.save(track);
    }

    /**
     * Returns all tracks.
     *
     * @return the set of tracks
     */
    public Set<Track> getTracks() {
        return trackRepository.findAll().stream().collect(Collectors.toSet());
    }

    /**
     * Returns a track by its ID.
     *
     * @param id the ID of the track
     * @return the track with the given ID, or null if not found
     */
    public Track getTrackById(UUID id) {
        return id == null ? null : trackRepository.findById(id).orElse(null);
    }
}
