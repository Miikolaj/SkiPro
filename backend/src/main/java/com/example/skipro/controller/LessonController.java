package com.example.skipro.controller;

import com.example.skipro.model.Lesson;
import com.example.skipro.model.Instructor;
import com.example.skipro.model.Client;
import com.example.skipro.service.ClientService;
import com.example.skipro.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/lessons")
public class LessonController {
    private final LessonService lessonService = new LessonService();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

    private Map<String, Object> clientToMap(Client client) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", client.getId());
        map.put("firstName", client.getFirstName());
        map.put("lastName", client.getLastName());
        return map;
    }

    private Map<String, Object> lessonToMap(Lesson lesson) {
        Map<String, Object> lessonMap = new HashMap<>();
        lessonMap.put("id", lesson.getId());
        lessonMap.put("date", lesson.getDateTime().format(DATE_TIME_FORMATTER));
        lessonMap.put("duration", formatDuration(lesson.getDuration()));
        lessonMap.put("status", lesson.getStatus());
        lessonMap.put("instructor", instructorToMap(lesson.getInstructor()));
        lessonMap.put("clients", lesson.getClients().stream()
                .map(this::clientToMap)
                .toList());
        // Add other fields as needed
        return lessonMap;
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m");
        return sb.toString().trim();
    }

    @GetMapping
    public List<Map<String, Object>> getLessons() {
        return lessonService.getAllPlannedLessons().stream()
                .map(this::lessonToMap)
                .toList();
    }

    @PostMapping
    public List<Map<String, Object>> getEnrolledLessons(@RequestParam String clientId) {
        return lessonService.getPlannedLessonsForClient(UUID.fromString(clientId)).stream()
                .map(this::lessonToMap)
                .toList();
    }
}