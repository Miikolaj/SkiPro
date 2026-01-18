package com.example.skipro.dto;

import java.util.UUID;

/**
 * Public-safe representation of Instructor for list/detail screens.
 */
public record InstructorDto(
        UUID id,
        String firstName,
        String lastName,
        String qualificationLevel,
        double rating
) {
}

