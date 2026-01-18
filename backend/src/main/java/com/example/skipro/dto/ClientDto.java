package com.example.skipro.dto;

import java.util.UUID;

/**
 * Data Transfer Object for Client entity.
 */
public record ClientDto(
        UUID id,
        String firstName,
        String lastName,
        int age,
        String experience
) { }