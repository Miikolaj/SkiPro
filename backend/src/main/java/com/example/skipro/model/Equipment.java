package com.example.skipro.model;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents a single piece of rental equipment (e.g., skis, snowboard, helmet) available at the resort.
 * <p>
 * Each {@code Equipment} instance is uniquely identified by an immutable {@link UUID}. The equipment’s
 * catalog name, size designation, and rental cost are stored as immutable fields. The {@code inUse}
 * flag indicates whether the item is currently rented out.
 * </p>
 */
@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String size;
    private int cost;

    private boolean inUse = false;

    /** PDF: technical condition (e.g., NEW/GOOD/USED/DAMAGED). */
    @Column(nullable = false)
    private String technicalCondition = "GOOD";

    /**
     * Constructs a new {@code Equipment} instance with the given attributes.
     *
     * @param name human‑readable name or model
     * @param size size designation (string, to allow units such as "cm" or labels like "L")
     * @param cost rental cost in basic currency units
     */
    protected Equipment() {
        // for JPA
    }

    public Equipment(String name, String size, int cost) {
        this(name, size, "GOOD", cost);
    }

    public Equipment(String name, String size, String technicalCondition, int cost) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Equipment name cannot be null/blank");
        }
        if (size == null || size.isBlank()) {
            throw new IllegalArgumentException("Equipment size cannot be null/blank");
        }
        if (technicalCondition == null || technicalCondition.isBlank()) {
            throw new IllegalArgumentException("technicalCondition cannot be null/blank");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("cost cannot be negative");
        }
        this.name = name;
        this.size = size;
        this.technicalCondition = technicalCondition;
        this.cost = cost;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public int getCost() {
        return cost;
    }

    public String getTechnicalCondition() {
        return technicalCondition;
    }

    public void setTechnicalCondition(String technicalCondition) {
        if (technicalCondition == null || technicalCondition.isBlank()) {
            throw new IllegalArgumentException("technicalCondition cannot be null/blank");
        }
        this.technicalCondition = technicalCondition;
    }

    @Override
    public String toString() {
        return name + " [" + size + "] " + (inUse ? "(In Use)" : "(Available)");
    }
}
