package com.example.skipro.dto;

import java.util.UUID;

/**
 * Read-only DTO for Employee base type.
 */
public record EmployeeDto(
        UUID id,
        String firstName,
        String lastName,
        String type,
        String role
) {
}

