package com.example.skipro.service;

import com.example.skipro.model.RescueTeam;
import com.example.skipro.model.Track;
import com.example.skipro.repository.RescueTeamRepository;
import com.example.skipro.repository.TrackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service responsible for managing rescue teams.
 */
@Service
public class RescueTeamService {

    private final RescueTeamRepository rescueTeamRepository;
    private final TrackRepository trackRepository;

    public RescueTeamService(
            RescueTeamRepository rescueTeamRepository,
            TrackRepository trackRepository
    ) {
        this.rescueTeamRepository = rescueTeamRepository;
        this.trackRepository = trackRepository;
    }

    @Transactional
    public RescueTeam createTeam(String name, UUID trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found: " + trackId));

        RescueTeam team = new RescueTeam(name, track, "default-channel", List.of(), null);
        return rescueTeamRepository.save(team);
    }

    // Membership is handled by RescueWorkerTeamAssignmentService (history-preserving assignments)

    @Transactional(readOnly = true)
    public List<RescueTeam> getTeamsForTrack(UUID trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found: " + trackId));

        // no custom SQL: filter in-memory, but still based on persisted entities
        return rescueTeamRepository.findAll().stream()
                .filter(t -> t.getAssignedTrack().getId().equals(track.getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RescueTeam> getAllTeams() {
        return rescueTeamRepository.findAll();
    }
}
