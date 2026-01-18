package com.example.skipro.dto;

import java.util.UUID;

/** Request body for rating an instructor after a finished lesson. */
public record RateInstructorRequest(
        UUID lessonId,
        UUID clientId,
        int rating
) {
}

