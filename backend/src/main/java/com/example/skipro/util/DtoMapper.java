package com.example.skipro.util;

import com.example.skipro.dto.ClientDto;
import com.example.skipro.dto.EquipmentDto;
import com.example.skipro.dto.EmployeeDto;
import com.example.skipro.dto.InstructorDto;
import com.example.skipro.dto.LessonDetailsDto;
import com.example.skipro.dto.LessonTileDto;
import com.example.skipro.dto.RentalClerkDto;
import com.example.skipro.dto.RentalDto;
import com.example.skipro.model.Client;
import com.example.skipro.model.Equipment;
import com.example.skipro.model.Employee;
import com.example.skipro.model.Instructor;
import com.example.skipro.model.Lesson;
import com.example.skipro.model.Rental;
import com.example.skipro.model.RentalClerk;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Central mapping helpers to avoid exposing JPA entities directly via JSON.
 */
public final class DtoMapper {
    private DtoMapper() {}

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm");
    private static final DateTimeFormatter DATE_TIME_WITH_SECONDS = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");

    public static ClientDto toClientDto(Client c) {
        if (c == null) return null;
        return new ClientDto(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getAge(),
                c.getExperience() == null ? null : c.getExperience().name()
        );
    }

    public static InstructorDto toInstructorDto(Instructor instructor) {
        if (instructor == null) return null;

        List<Integer> ratings = instructor.getRatings();
        double avgRating = 0.0;
        if (ratings != null && !ratings.isEmpty()) {
            avgRating = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            avgRating = Math.round(avgRating * 10.0) / 10.0;
        }

        return new InstructorDto(
                instructor.getId(),
                instructor.getFirstName(),
                instructor.getLastName(),
                instructor.getQualificationLevel(),
                avgRating
        );
    }

    public static LessonTileDto toLessonTileDto(Lesson lesson, long clientsCount) {
        if (lesson == null) return null;
        return new LessonTileDto(
                lesson.getId(),
                lesson.getDateTime().format(DATE_TIME_FORMATTER),
                formatDuration(lesson.getDuration()),
                lesson.getStatus() == null ? null : lesson.getStatus().name(),
                toInstructorDto(lesson.getInstructor()),
                clientsCount
        );
    }

    public static LessonDetailsDto toLessonDetailsDto(Lesson lesson, long clientsCount) {
        if (lesson == null) return null;

        List<ClientDto> clients = (lesson.getClients() == null)
                ? List.of()
                : lesson.getClients().stream().map(DtoMapper::toClientDto).toList();

        return new LessonDetailsDto(
                lesson.getId(),
                lesson.getDateTime().format(DATE_TIME_FORMATTER),
                formatDuration(lesson.getDuration()),
                lesson.getStatus() == null ? null : lesson.getStatus().name(),
                toInstructorDto(lesson.getInstructor()),
                clientsCount,
                clients
        );
    }

    public static EquipmentDto toEquipmentDto(Equipment e) {
        if (e == null) return null;
        return new EquipmentDto(
                e.getId(),
                e.getName(),
                e.getSize(),
                e.getCost(),
                e.isInUse()
        );
    }

    public static EmployeeDto toEmployeeDto(Employee e) {
        if (e == null) return null;
        return new EmployeeDto(
                e.getId(),
                e.getFirstName(),
                e.getLastName(),
                e.getClass().getSimpleName(),
                e.getRole()
        );
    }

    public static RentalClerkDto toRentalClerkDto(RentalClerk c) {
        if (c == null) return null;
        return new RentalClerkDto(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getRentalsHandled()
        );
    }

    public static RentalDto toRentalDto(Rental r) {
        if (r == null) return null;
        return new RentalDto(
                r.getId(),
                r.getStatus() == null ? null : r.getStatus().name(),
                r.getRentalCost(),
                r.getStartDate() == null ? null : r.getStartDate().format(DATE_TIME_WITH_SECONDS),
                r.getEndDate() == null ? null : r.getEndDate().format(DATE_TIME_WITH_SECONDS),
                toEquipmentDto(r.getEquipment()),
                toClientDto(r.getClient()),
                toRentalClerkDto(r.getRentalClerk())
        );
    }

    private static String formatDuration(Duration duration) {
        if (duration == null) return "";
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m");
        return sb.toString().trim();
    }
}
