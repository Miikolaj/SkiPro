package com.example.skipro.model;

import com.example.skipro.model.enums.Status;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a specialised team of {@link RescueWorker}s assigned to a particular ski {@link Track}.
 * <p>
 * A {@code RescueTeam} has a unique immutable {@link UUID} and stores operational metadata such as
 * the radio/voice {@link #communicationChannel}, a list of {@link #specialEquipment} carried, and the current
 * operational {@link #status}. Team membership is maintained in an internal {@link Set} to avoid duplicates;
 * helper methods ensure referential integrity by updating the {@code RescueWorker}'s back-reference.
 * </p>
 */
public class RescueTeam implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id = UUID.randomUUID();
    private final String name;
    private Track assignedTrack;
    private Set<RescueWorker> members = new HashSet<>();
    private String communicationChannel;
    private List<String> specialEquipment = new ArrayList<>();
    private Status status = Status.PENDING;

    /**
     * Constructs a new {@code RescueTeam} linked to the specified track.
     * The team automatically registers itself with the track via {@link Track#addRescueTeam(RescueTeam)}.
     *
     * @param name               team name or call-sign
     * @param track              track the team is responsible for (non-null)
     * @param communicationChannel primary communication channel (e.g., radio frequency)
     * @param specialEquipment   initial list of special equipment (may be {@code null})
     * @param status             initial {@link Status} (defaults to {@link Status#PENDING} if {@code null})
     * @throws IllegalArgumentException if {@code track} is {@code null}
     */
    public RescueTeam(String name, Track track, String communicationChannel, List<String> specialEquipment, Status status) {
        if (track == null) {
            throw new IllegalArgumentException("Rescue team must be assigned to a track.");
        }
        this.name = name;
        this.assignedTrack = track;
        this.communicationChannel = communicationChannel;
        if (specialEquipment != null) {
            this.specialEquipment = new ArrayList<>(specialEquipment);
        }
        if (status != null) {
            this.status = status;
        }
        track.addRescueTeam(this);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Track getAssignedTrack() {
        return assignedTrack;
    }

    public String getCommunicationChannel() {
        return communicationChannel;
    }

    public void setCommunicationChannel(String communicationChannel) {
        this.communicationChannel = communicationChannel;
    }

    public List<String> getSpecialEquipment() {
        return Collections.unmodifiableList(specialEquipment);
    }

    public void addSpecialEquipment(String equipment) {
        if (equipment == null || equipment.isBlank()) {
            throw new IllegalArgumentException("Equipment cannot be null or blank.");
        }
        specialEquipment.add(equipment);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addMember(RescueWorker member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
        if (members.contains(member)) {
            throw new IllegalArgumentException("Member is already part of this rescue team.");
        }
        members.add(member);
        member.setRescueTeam(this);
    }

    public Set<RescueWorker> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    @Override
    public String toString() {
        return name + " on track " + assignedTrack.getName() +
                ", channel: " + communicationChannel +
                ", status: " + status +
                ", equipment: " + specialEquipment;
    }
}