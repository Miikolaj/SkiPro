package com.example.skipro.model;

import com.example.skipro.model.enums.RentalStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a single equipment rental transaction between a {@link Client} and the resort.
 * <p>
 * A {@code Rental} captures the equipment item, the renting client, the rental period, its current
 * {@link RentalStatus}, and the rental cost charged. Each rental is uniquely identified by a
 * random immutable {@link UUID} generated at creation time.
 * </p>
 */
public class Rental implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID ID = UUID.randomUUID();
    private final Equipment equipment;
    private final Client client;
    private final LocalDateTime startDate;
    private LocalDateTime endDate;
    private RentalStatus status;
    private int rentalCost;

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
