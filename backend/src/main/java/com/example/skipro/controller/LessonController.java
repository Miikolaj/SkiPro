package com.example.skipro.controller;

import com.example.skipro.model.Lesson;
import com.example.skipro.model.Instructor;
import com.example.skipro.model.Client;
import com.example.skipro.service.ClientService;
import com.example.skipro.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/lessons")
public class LessonController {
    private final LessonService lessonService = new LessonService();
    private final ClientService clientService = new ClientService();

    private Map<String, Object> instructorToMap(Instructor instructor) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", instructor.getId());
        map.put("qualificationLevel", instructor.getQualificationLevel());
        map.put("ratings", instructor.getRatings());
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
        lessonMap.put("dateTime", lesson.getDateTime());
        lessonMap.put("duration", lesson.getDuration());
        lessonMap.put("status", lesson.getStatus());
        lessonMap.put("instructor", instructorToMap(lesson.getInstructor()));
        lessonMap.put("clients", lesson.getClients().stream()
                .map(this::clientToMap)
                .toList());
        // Add other fields as needed
        return lessonMap;
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