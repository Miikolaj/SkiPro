package model;

import model.enums.Status;
import java.util.*;

public class RescueTeam {
    private final UUID id = UUID.randomUUID();
    private final String name;
    private Track assignedTrack;
    private Set<RescueWorker> members = new HashSet<>();
    private String communicationChannel;
    private List<String> specialEquipment = new ArrayList<>();
    private Status status = Status.PENDING;

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