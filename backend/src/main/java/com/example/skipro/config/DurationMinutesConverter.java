package com.example.skipro.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

/**
 * Stores {@link Duration} as total minutes in the database.
 *
 * PostgreSQL has no native duration type that maps cleanly in JPA without extra setup,
 * so its stored as a numeric value (minutes).
 */
@Converter(autoApply = true)
public class DurationMinutesConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        if (attribute == null) return null;
        return attribute.toMinutes();
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        if (dbData == null) return null;
        return Duration.ofMinutes(dbData);
    }
}
