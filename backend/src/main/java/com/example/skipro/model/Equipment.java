package com.example.skipro.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a single piece of rental equipment (e.g., skis, snowboard, helmet) available at the resort.
 * <p>
 * Each {@code Equipment} instance is uniquely identified by an immutable {@link UUID}. The equipment’s
 * catalog name, size designation, and rental cost are stored as immutable fields. The {@code inUse}
 * flag indicates whether the item is currently rented out.
 * </p>
 */
public class Equipment implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id = UUID.randomUUID();
    private final String name;
    private final String size;
    private final int cost;

    private boolean inUse = false;

    /**
     * Constructs a new {@code Equipment} instance with the given attributes.
     *
     * @param name  human‑readable name or model
     * @param size  size designation (string, to allow units such as "cm" or labels like "L")
     * @param cost  rental cost in basic currency units
     */
    public Equipment(String name, String size,int cost) {
        this.name = name;
        this.size = size;
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

    @Override
    public String toString() {
        return name + " [" + size + "] " + (inUse ? "(In Use)" : "(Available)");
    }
}
