package com.example.skipro.service;

import com.example.skipro.model.*;
import com.example.skipro.util.PersistenceManager;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service responsible for managing rescue teams and persisting them to a file.
 */
@Service
public class RescueTeamService {
    private final PersistenceManager<RescueTeam> persistence = new PersistenceManager<>("src/main/java/com/example/skipro/data/rescue_team.ser"); //Name of the file used for saving rescue teams.
    private List<RescueTeam> rescueTeams = new ArrayList<>();

    /**
     * Constructs a RescueTeamService and loads teams from file.
     */
    public RescueTeamService() {
        rescueTeams = persistence.load();
    }

    /**
     * Creates a new {@link RescueTeam}, registers it in the system, and returns it.
     *
     * @param name  the team name
     * @param track the track the team is responsible for
     * @return the newly created team
     */
    public RescueTeam createTeam(String name, Track track) {
        RescueTeam team = new RescueTeam(name, track, "default-channel", Collections.emptyList(), null);
        rescueTeams.add(team);
        persistence.save(rescueTeams);
        return team;
    }

    /**
     * Adds a rescue worker to the specified team.
     *
     * @param team   the team to which the worker will be added (must already be registered)
     * @param worker the worker to add
     * @throws IllegalArgumentException if the team is not registered in the system
     */
    public void addMemberToTeam(RescueTeam team, RescueWorker worker) {
        if (!rescueTeams.contains(team)) {
            throw new IllegalArgumentException("Team is not registered in the system.");
        }
        team.addMember(worker);
        persistence.save(rescueTeams);
    }

    /**
     * Returns all teams assigned to a specific track.
     *
     * @param track the track
     * @return list of teams responsible for the provided track
     */
    public List<RescueTeam> getTeamsForTrack(Track track) {
        return rescueTeams.stream()
                .filter(team -> team.getAssignedTrack().equals(track))
                .toList();
    }

    /**
     * Returns an unmodifiable list of all teams.
     *
     * @return list of all registered teams
     */
    public List<RescueTeam> getAllTeams() {
        return Collections.unmodifiableList(rescueTeams);
    }
}
