package com.example.skipro.service;

import com.example.skipro.model.Track;
import com.example.skipro.util.PersistenceManager;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Service responsible for managing ski tracks and persisting them to a file.
 */
@Service
public class TrackService {
    private Set<Track> tracks = new HashSet<>(); //Set containing all tracks.
    private final PersistenceManager<Track> persistence = new PersistenceManager<>("src/main/java/com/example/skipro/data/tracks.ser"); //Name of the file used for saving tracks.

    /**
     * Constructs a TrackService and loads tracks from file.
     */
    public TrackService() {
        tracks = persistence.loadSet();
    }

    /**
     * Adds a track and saves the updated set to file.
     *
     * @param track the track to add
     */
    public void addTrack(Track track) {
        tracks.add(track);
        persistence.saveSet(tracks);
    }

    /**
     * Returns all tracks.
     *
     * @return the set of tracks
     */
    public Set<Track> getTracks() {
        return tracks;
    }
}
