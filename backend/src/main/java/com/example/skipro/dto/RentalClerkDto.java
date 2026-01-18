package com.example.skipro.dto;

import java.util.UUID;

public record RentalClerkDto(
        UUID id,
        String firstName,
        String lastName,
        int rentalsHandled
) {
}



