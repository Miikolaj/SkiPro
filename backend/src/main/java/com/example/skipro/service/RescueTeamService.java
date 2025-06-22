package com.example.skipro.service;

import com.example.skipro.model.RescueTeam;
import com.example.skipro.model.RescueWorker;
import com.example.skipro.model.Resort;
import com.example.skipro.model.Track;
import com.example.skipro.model.enums.TrackDifficulty;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RescueTeamService {
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/rescue_team.ser";
    private final List<RescueTeam> rescueTeams = new ArrayList<>();

    public RescueTeam createTeam(String name, Track track) {
        RescueTeam team = new RescueTeam(name, track, "default-channel", Collections.emptyList(), null);
        rescueTeams.add(team);
        return team;
    }

    public void addMemberToTeam(RescueTeam team, RescueWorker worker) {
        if(!rescueTeams.contains(team)){
            throw new IllegalArgumentException("Team is not registered in the system.");
        }

        team.addMember(worker);
    }

    public List<RescueTeam> getTeamsForTrack(Track track) {
        return rescueTeams.stream()
                .filter(team -> team.getAssignedTrack().equals(track))
                .toList();
    }

    public List<RescueTeam> getAllTeams() {
        return Collections.unmodifiableList(rescueTeams);
    }

    public Optional<RescueTeam> getTeamByName(String name) {
        return rescueTeams.stream()
                .filter(team -> team.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public void saveTeamsToFile() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(rescueTeams);
        }
    }

    public void loadTeamsFromFile() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<RescueTeam> loaded = (List<RescueTeam>) ois.readObject();
            rescueTeams.clear();
            rescueTeams.addAll(loaded);
        }
    }
}
