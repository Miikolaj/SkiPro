package com.example.skipro.service;

import com.example.skipro.model.Rental;
import com.example.skipro.model.RescueTeam;
import com.example.skipro.model.RescueWorker;
import com.example.skipro.model.Track;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service responsible for managing rescue teams and persisting them to a file.
 */
public class RescueTeamService {
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/rescue_team.ser"; //Name of the file used for saving rescue teams.
    private  List<RescueTeam> rescueTeams = new ArrayList<>(); //List containing all rescue teams.

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
        save();
        return team;
    }

    public RescueTeamService() {
        loadTeamsFromFile();
    }

    /**
     * Adds a rescue worker to the specified team.
     *
     * @param team   the team to which the worker will be added (must already be registered)
     * @param worker the worker to add
     * @throws IllegalArgumentException if the team is not registered in the system
     */
    public void addMemberToTeam(RescueTeam team, RescueWorker worker) {
        if(!rescueTeams.contains(team)){
            throw new IllegalArgumentException("Team is not registered in the system.");
        }
        team.addMember(worker);
        save();
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

    /**
     * Retrieves a team by name, ignoring case.
     *
     * @param name the team name
     * @return an {@link Optional} containing the team if found, otherwise empty
     */
    public Optional<RescueTeam> getTeamByName(String name) {
        return rescueTeams.stream()
                .filter(team -> team.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Saves the list of rescue teams to a file.
     */
    public void save()  {
        File file = new File(FILE_NAME);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(rescueTeams);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the list of rescue teams from a file if it exists.
     *
     * @throws IOException            if an I/O error occurs during reading
     * @throws ClassNotFoundException if the file does not contain a valid List<RescueTeam>
     */
    public void loadTeamsFromFile()  {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            rescueTeams = (List<RescueTeam>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
