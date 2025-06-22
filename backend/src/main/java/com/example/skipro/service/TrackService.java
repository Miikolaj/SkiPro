package com.example.skipro.service;

import com.example.skipro.model.Resort;
import com.example.skipro.model.Track;
import com.example.skipro.model.enums.TrackDifficulty;

import java.io.*;
import java.util.*;

public class TrackService {
    private Set<Track> tracks = new HashSet<>();
    private static final String FILE_NAME = "/data/tracks.ser";

    public TrackService() {
        try {
            loadTracksFromFile();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading tracks: " + e.getMessage());
        }
    }

    public void addTrack(Track track) {
        tracks.add(track);
        try {
            saveTracksToFile();
        } catch (IOException e) {
            System.err.println("Error saving tracks: " + e.getMessage());
        }
    }

    public Set<Track> getTracks() {
        return tracks;
    }

    public void saveTracksToFile() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tracks);
        }
    }

    public void loadTracksFromFile() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            tracks = (Set<Track>) ois.readObject();
        }
    }
}
