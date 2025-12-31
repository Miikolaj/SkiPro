package com.example.skipro.controller;

import com.example.skipro.model.Instructor;
import com.example.skipro.model.Lesson;
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
     * Returns all <em>planned</em> lessons that the given client is <strong>not</strong> enrolled in.
     *
     * @param clientId the client identifier
     * @return list of lessons represented as maps suitable for JSON serialization
     */
    @PostMapping("/planned")
    public List<Map<String, Object>> getLessons(@RequestParam String clientId) {
        return lessonService.getPlannedLessonsWithoutClient(UUID.fromString(clientId)).stream()
                .map(LessonMapper::lessonToTileMap)
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
                .map(LessonMapper::lessonToTileMap)
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
                .map(LessonMapper::lessonToTileMap)
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

    /**
     * Creates a new lesson with the specified time, duration, and instructor.
     *
     * @param time         ISO_LOCAL_DATE_TIME formatted start time of the lesson
     * @param duration     ISO-8601 formatted duration (e.g., PT1H for 1 hour)
     * @param instructorId the instructor identifier
     * @return HTTP 200 if creation succeeded
     */
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

    /**
     * Removes the specified client from the given lesson.
     *
     * @param lessonId the lesson identifier
     * @param clientId the client identifier
     * @return true if removal succeeded; false otherwise
     */
    @PostMapping("/remove")
    public boolean removeClientFromLesson(@RequestParam String lessonId, @RequestParam String clientId) {
        UUID uuid = UUID.fromString(lessonId);
        UUID uuid2 = UUID.fromString(clientId);
        return lessonService.removeClientFromLesson(uuid, uuid2);
    }

    /**
     * Lazy-load: returns clients enrolled in a given lesson.
     */
    @GetMapping("/{lessonId}/clients")
    public ResponseEntity<List<Map<String, Object>>> getLessonClients(@PathVariable String lessonId) {
        Lesson lesson = lessonService.getLessonById(UUID.fromString(lessonId));
        if (lesson == null) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, Object>> clients = (lesson.getClients() == null ? List.<Map<String, Object>>of() : lesson.getClients().stream()
                .map(c -> {
                    Map<String, Object> clientMap = new HashMap<>();
                    clientMap.put("firstName", c.getFirstName());
                    clientMap.put("lastName", c.getLastName());
                    clientMap.put("id", c.getId());
                    return clientMap;
                })
                .toList());

        return ResponseEntity.ok(clients);
    }
}