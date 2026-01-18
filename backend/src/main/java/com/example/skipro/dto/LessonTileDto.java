package com.example.skipro.dto;

import java.util.UUID;

/**
 * Lightweight Lesson DTO for list views.
 */
public record LessonTileDto(
        UUID id,
        String date,
        String duration,
        String status,
        InstructorDto instructor,
        long clientsCount
) {
}

