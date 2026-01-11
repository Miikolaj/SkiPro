package com.example.skipro.model;

import com.example.skipro.model.enums.RentalStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rentals")
public class Rental implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    private int rentalCost;

    protected Rental() {
        // for JPA
    }

    /**
     * Creates a new <strong>active</strong> rental for the given equipment and client.
     * The equipment is assumed to be available; its {@code inUse} flag must be set by callers.
     *
     * @param equipment the equipment item being rented (non-null)
     * @param client    the client renting the equipment (non-null)
     */
    public Rental(Equipment equipment, Client client) {
        this.equipment = equipment;
        this.client = client;
        this.startDate = LocalDateTime.now();
        this.status = RentalStatus.ACTIVE;
        this.rentalCost = equipment.getCost();
    }

    public void returnEquipment() {
        if (status != RentalStatus.ACTIVE) {
            throw new IllegalStateException("Equipment has already been returned.");
        }
        this.endDate = LocalDateTime.now();
        this.status = RentalStatus.RETURNED;
        this.equipment.setInUse(false);
    }

    public UUID getId() {
        return id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Client getClient() {
        return client;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public int getRentalCost() {
        return rentalCost;
    }

    public void setRentalCost(int rentalCost) {
        this.rentalCost = rentalCost;
    }

    @Override
    public String toString() {
        return equipment + " rented by " + client.getFirstName() + " " + client.getLastName() + " [" + status + "]";
    }
}
