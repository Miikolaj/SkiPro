package com.example.skipro.controller;

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
    private final LessonService lessonService;
    private final InstructorService instructorService;

    public LessonController(LessonService lessonService, InstructorService instructorService) {
        this.lessonService = lessonService;
        this.instructorService = instructorService;
    }

    /**
     * Returns all <em>planned</em> lessons that the given client is <strong>not</strong> enrolled in.
     *
     * @param clientId the client identifier
     * @return list of lessons represented as maps suitable for JSON serialization
     */
    @PostMapping("/planned")
    public List<Map<String, Object>> getLessons(@RequestParam String clientId) {
        return lessonService.getPlannedLessonsWithoutClient(UUID.fromString(clientId)).stream()
                .map(lesson -> {
                    Map<String, Object> map = LessonMapper.lessonToTileMap(lesson);
                    map.put("clientsCount", lessonService.countClients(lesson.getId()));
                    return map;
                })
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
                .map(lesson -> {
                    Map<String, Object> map = LessonMapper.lessonToTileMap(lesson);
                    map.put("clientsCount", lessonService.countClients(lesson.getId()));
                    return map;
                })
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
                .map(lesson -> {
                    Map<String, Object> map = LessonMapper.lessonToTileMap(lesson);
                    map.put("clientsCount", lessonService.countClients(lesson.getId()));
                    return map;
                })
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
        final UUID lessonUuid;
        final UUID clientUuid;
        try {
            lessonUuid = UUID.fromString(lessonId);
            clientUuid = UUID.fromString(clientId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        // 404 if either resource doesn't exist
        if (lessonService.getLessonById(lessonUuid) == null) {
            return ResponseEntity.notFound().build();
        }
        if (lessonService.getClientById(clientUuid) == null) {
            return ResponseEntity.notFound().build();
        }

        boolean enrolled = lessonService.enrollClientToLesson(lessonUuid, clientUuid);
        return enrolled ? ResponseEntity.ok().build() : ResponseEntity.status(409).build();
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
        final UUID instructorUuid;
        try {
            instructorUuid = UUID.fromString(instructorId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        Instructor instructor = instructorService.getInstructorById(instructorUuid);
        if (instructor == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            lessonService.createLesson(
                    LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    Duration.parse(duration),
                    instructor
            );
        } catch (Exception e) {
            // parsing errors etc.
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Removes the specified client from the given lesson.
     *
     * @param lessonId the lesson identifier
     * @param clientId the client identifier
     * @return HTTP 200 if removal succeeded; 404 if lesson/client not found; 409 if removal failed
     */
    @PostMapping("/remove")
    public ResponseEntity<Void> removeClientFromLesson(@RequestParam String lessonId, @RequestParam String clientId) {
        final UUID lessonUuid;
        final UUID clientUuid;
        try {
            lessonUuid = UUID.fromString(lessonId);
            clientUuid = UUID.fromString(clientId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        // 404 if either resource doesn't exist
        if (lessonService.getLessonById(lessonUuid) == null) {
            return ResponseEntity.notFound().build();
        }
        if (lessonService.getClientById(clientUuid) == null) {
            return ResponseEntity.notFound().build();
        }

        boolean removed = lessonService.removeClientFromLesson(lessonUuid, clientUuid);
        return removed ? ResponseEntity.ok().build() : ResponseEntity.status(409).build();
    }

    /**
     * Lazy-load: returns clients enrolled in a given lesson.
     */
    @GetMapping("/{lessonId}/clients")
    public ResponseEntity<List<Map<String, Object>>> getLessonClients(@PathVariable String lessonId) {
        final UUID lessonUuid;
        try {
            lessonUuid = UUID.fromString(lessonId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        if (lessonService.getLessonById(lessonUuid) == null) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, Object>> clients = lessonService.getLessonClients(lessonUuid).stream()
                .map(c -> {
                    Map<String, Object> clientMap = new HashMap<>();
                    clientMap.put("firstName", c.getFirstName());
                    clientMap.put("lastName", c.getLastName());
                    clientMap.put("id", c.getId());
                    return clientMap;
                })
                .toList();

        return ResponseEntity.ok(clients);
    }
}