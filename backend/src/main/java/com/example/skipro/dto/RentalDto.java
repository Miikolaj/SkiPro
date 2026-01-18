package com.example.skipro.dto;

import java.util.UUID;

/**
 * Rental DTO for list/detail views.
 * Contains small nested DTOs to avoid extra round-trips in GUI.
 */
public record RentalDto(
        UUID id,
        String status,
        int rentalCost,
        String startDate,
        String endDate,
        EquipmentDto equipment,
        ClientDto client,
        RentalClerkDto rentalClerk
) {
}

