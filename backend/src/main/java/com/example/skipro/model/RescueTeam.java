package com.example.skipro.model;

import com.example.skipro.model.enums.Status;
import jakarta.persistence.*;

import java.util.*;

/**
 * Represents a specialised team of {@link RescueWorker}s assigned to a particular ski {@link Track}.
 */
@Entity
@Table(name = "rescue_teams")
public class RescueTeam {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "track_id", nullable = false)
    private Track assignedTrack;

    @OneToMany(mappedBy = "rescueTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RescueWorkerTeamAssignment> assignments = new HashSet<>();

    private String communicationChannel;

    @ElementCollection
    @CollectionTable(name = "rescue_team_special_equipment", joinColumns = @JoinColumn(name = "rescue_team_id"))
    @Column(name = "equipment")
    private List<String> specialEquipment = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    protected RescueTeam() {
        // for JPA
    }

    /**
     * Constructs a new {@code RescueTeam} linked to the specified track.
     * The team automatically registers itself with the track via {@link Track#addRescueTeam(RescueTeam)}.
     *
     * @param name                 team name or call-sign
     * @param track                track the team is responsible for (non-null)
     * @param communicationChannel primary communication channel (e.g., radio frequency)
     * @param specialEquipment     initial list of special equipment (may be {@code null})
     * @param status               initial {@link Status} (defaults to {@link Status#PENDING} if {@code null})
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
        // keep in-memory navigation consistent for existing code paths
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

    /**
     * Current members based on active assignments.
     */
    @Transient
    public Set<RescueWorker> getMembers() {
        return assignments.stream()
                .filter(RescueWorkerTeamAssignment::isActive)
                .map(RescueWorkerTeamAssignment::getRescueWorker)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    @Override
    public String toString() {
        return name + " on track " + (assignedTrack != null ? assignedTrack.getName() : "<track>") +
                ", channel: " + communicationChannel +
                ", status: " + status +
                ", equipment: " + specialEquipment;
    }
}