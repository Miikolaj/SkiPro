package com.example.skipro.dto;

import java.util.UUID;

public record EquipmentDto(
        UUID id,
        String name,
        String size,
        int cost,
        boolean inUse
) {
}

