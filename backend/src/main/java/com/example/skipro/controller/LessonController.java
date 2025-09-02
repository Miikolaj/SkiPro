package com.example.skipro.controller;

import com.example.skipro.model.Lesson;
import com.example.skipro.model.Instructor;
import com.example.skipro.service.InstructorService;
import com.example.skipro.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.skipro.util.LessonMapper;
import java.time.Duration;
import java.time.LocalDateTime;
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
    private final InstructorService instructorService = new InstructorService();

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
                .map(LessonMapper::lessonToMap)
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
                .map(LessonMapper::lessonToMap)
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
                .map(LessonMapper::lessonToMap)
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
        System.out.println("Enrolling client " + clientId + " in lesson " + lessonId);
        boolean enrolled = lessonService.enrollClientToLesson(UUID.fromString(lessonId), UUID.fromString(clientId));
        if (enrolled) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestParam String time, @RequestParam String duration, @RequestParam String instructorId) {
        Instructor instructor = instructorService.getInstructorById(instructorId);

        lessonService.createLesson(
                LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                Duration.parse(duration),
                instructor
        );

        return ResponseEntity.ok().build();
    }
}