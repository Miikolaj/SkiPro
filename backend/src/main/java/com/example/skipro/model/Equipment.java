package com.example.skipro.model;

import java.util.UUID;

public class Equipment {
    private final UUID id = UUID.randomUUID();
    private final String name;
    private final String size;
    private final int cost;

    private boolean inUse = false;

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
