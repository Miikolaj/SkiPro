package com.example.skipro.service;

import com.example.skipro.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service responsible for managing rescue teams.
 *
 * NOTE: This service is currently kept in-memory. If rescue teams are part of the final graded data set,
 * migrate them to JPA entities + repositories.
 */
@Service
public class RescueTeamService {
    private final List<RescueTeam> rescueTeams = new ArrayList<>();

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
