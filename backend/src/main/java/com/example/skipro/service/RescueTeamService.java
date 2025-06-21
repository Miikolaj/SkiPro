package com.example.skipro.service;

import com.example.skipro.model.RescueTeam;
import com.example.skipro.model.RescueWorker;
import com.example.skipro.model.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RescueTeamService {
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
}
