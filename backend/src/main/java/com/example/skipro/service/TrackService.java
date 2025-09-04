package com.example.skipro.service;

import com.example.skipro.model.RescueWorker;
import com.example.skipro.model.Resort;
import com.example.skipro.model.Track;
import com.example.skipro.model.enums.TrackDifficulty;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Service responsible for managing ski tracks and persisting them to a file.
 */
@Service
public class TrackService {
    private Set<Track> tracks = new HashSet<>(); //Set containing all tracks.
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/tracks.ser"; //Name of the file used for saving tracks.
    /**
     * Constructs a TrackService and loads tracks from file.
     */
    public TrackService() {
        try {
            loadTracksFromFile();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading tracks: " + e.getMessage());
        }
    }
    /**
     * Adds a track and saves the updated set to file.
     *
     * @param track the track to add
     */
    public void addTrack(Track track) {
        tracks.add(track);
        try {
            saveTracksToFile();
        } catch (IOException e) {
            System.err.println("Error saving tracks: " + e.getMessage());
        }
    }
    /**
     * Returns all tracks.
     *
     * @return the set of tracks
     */
    public Set<Track> getTracks() {
        return tracks;
    }
    /**
     * Saves the set of tracks to a file.
     *
     * @throws IOException if an I/O error occurs during writing
     */
    public void saveTracksToFile() throws IOException {
        File file = new File(FILE_NAME);
        file.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(tracks);
        }
    }

    public void loadTracksFromFile() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        if (!file.exists() || file.length() == 0) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Set<Track> loadedSet = (Set<Track>) ois.readObject();
            tracks.clear();
            tracks.addAll(loadedSet);
        }
    }
}
