package com.example.skipro.dto;

import java.util.List;
import java.util.UUID;

/**
 * Detailed Lesson DTO including enrolled clients.
 */
public record LessonDetailsDto(
        UUID id,
        String date,
        String duration,
        String status,
        InstructorDto instructor,
        long clientsCount,
        List<ClientDto> clients
) {
}

