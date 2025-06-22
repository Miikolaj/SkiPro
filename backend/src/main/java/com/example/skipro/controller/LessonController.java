package com.example.skipro.controller;

import com.example.skipro.model.Lesson;
import com.example.skipro.model.Instructor;
import com.example.skipro.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * REST controller exposing endpoints for viewing, enrolling in, and managing ski lessons.
 */
@RestController
@RequestMapping("/lessons")
public class LessonController {
    /**
     * Service used to query and modify lesson data.
     */
    private final LessonService lessonService = new LessonService();
    /**
     * Formatter used to render lesson dates in responses.
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Returns all <em>planned</em> lessons that the given client is <strong>not</strong> enrolled in.
     *
     * @param clientId the client identifier
     * @return list of lessons represented as maps suitable for JSON serialization
     */
    @PostMapping("/planned")
    public List<Map<String, Object>> getLessons(@RequestParam String clientId) {
        return lessonService.getPlannedLessonsWithoutClient(UUID.fromString(clientId)).stream()
                .map(this::lessonToMap)
                .toList();
    }

    /**
     * Returns all <em>planned</em> lessons that the given client <strong>is</strong> enrolled in.
     *
     * @param clientId the client identifier
     * @return list of enrolled lessons represented as maps
     */
    @PostMapping
    public List<Map<String, Object>> getEnrolledLessons(@RequestParam String clientId) {
        return lessonService.getPlannedLessonsForClient(UUID.fromString(clientId)).stream()
                .map(this::lessonToMap)
                .toList();
    }

    /**
     * Returns all <em>finished</em> lessons for the given client.
     *
     * @param clientId the client identifier
     * @return list of finished lessons represented as maps
     */
    @PostMapping("/finished")
    public List<Map<String, Object>> getFinishedLessons(@RequestParam String clientId) {
        return lessonService.getFinishedLessonsForClient(UUID.fromString(clientId)).stream()
                .map(this::lessonToMap)
                .toList();
    }

    /**
     * Enrolls the specified client in the given lesson.
     *
     * @param lessonId the lesson identifier
     * @param clientId the client identifier
     * @return HTTP 200 if enrollment succeeded; 404 if the lesson or client could not be found or enrollment failed
     */
    @PostMapping("/enroll")
    public ResponseEntity<Void> enrollClientInLesson(@RequestParam String lessonId, @RequestParam String clientId) {
        boolean enrolled = lessonService.enrollClientToLesson(UUID.fromString(lessonId), UUID.fromString(clientId));
        if (enrolled) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    // Helper mapping utilities (not exposed as endpoints)
    /**
     * Converts an {@link Instructor} entity into a map representation suitable for JSON output.
     *
     * @param instructor the instructor
     * @return map containing instructor fields
     */
    private Map<String, Object> instructorToMap(Instructor instructor) {
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", instructor.getFirstName());
        map.put("lastName", instructor.getLastName());
        map.put("id", instructor.getId());
        map.put("qualificationLevel", instructor.getQualificationLevel());
        List<Integer> ratings = instructor.getRatings();
        double avgRating = 0.0;
        if (ratings != null && !ratings.isEmpty()) {
            avgRating = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            avgRating = Math.round(avgRating * 10.0) / 10.0;
        }
        map.put("rating", avgRating);
        return map;
    }

    /**
     * Converts a {@link Lesson} entity into a map representation suitable for JSON output.
     *
     * @param lesson the lesson
     * @return map containing lesson fields
     */
    private Map<String, Object> lessonToMap(Lesson lesson) {
        Map<String, Object> lessonMap = new HashMap<>();
        lessonMap.put("id", lesson.getId());
        lessonMap.put("date", lesson.getDateTime().format(DATE_TIME_FORMATTER));
        lessonMap.put("duration", formatDuration(lesson.getDuration()));
        lessonMap.put("status", lesson.getStatus());
        lessonMap.put("instructor", instructorToMap(lesson.getInstructor()));
        int clientsCount = lesson.getClients() == null ? 0 : lesson.getClients().size();
        lessonMap.put("clientsCount", clientsCount);
        return lessonMap;
    }

    /**
     * Formats a {@link Duration} as a human‑readable string (e.g., {@code "1h 30m"}).
     *
     * @param duration the duration
     * @return formatted duration string
     */
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m");
        return sb.toString().trim();
    }
}