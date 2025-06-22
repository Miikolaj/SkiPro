package com.example.skipro.service;

import com.example.skipro.model.Client;
import com.example.skipro.model.Instructor;
import com.example.skipro.model.Lesson;
import com.example.skipro.model.enums.LessonStatus;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.time.Duration;

public class LessonService {
    private static final String LESSONS_FILE = "src/main/java/com/example/skipro/data/lessons.ser";
    private final List<Lesson> lessonRegistry = new ArrayList<>();
    private final ClientService clientService = new ClientService();

    public LessonService() {
        loadLessons();
    }

    public List<Lesson> getPlannedLessonsWithoutClient(UUID clientId) {
        return lessonRegistry.stream()
                .filter(l -> l.getStatus() == LessonStatus.PLANNED)
                .filter(l -> l.getClients().stream()
                        .noneMatch(c -> c.getId().equals(clientId)))
                .toList();
    }

    public List<Lesson> getPlannedLessonsForClient(UUID clientId) {
        return lessonRegistry.stream()
                .filter(l -> l.getClients().stream()
                        .anyMatch(c -> c.getId().equals(clientId)))
                .filter(l -> l.getStatus() == LessonStatus.PLANNED)
                .toList();
    }

    public List<Lesson> getFinishedLessonsForClient(UUID clientId) {
        return lessonRegistry.stream()
                .filter(l -> l.getClients().stream()
                        .anyMatch(c -> c.getId().equals(clientId)))
                .filter(l -> l.getStatus() == LessonStatus.FINISHED)
                .toList();
    }

    public boolean enrollClientToLesson(UUID lessonId, UUID clientId) {
        Lesson lesson = getLessonById(lessonId);
        if (lesson == null) return false;
        Client client = clientService.getClientById(clientId);
        if (client == null) return false;
        try {
            lesson.enrollClient(client);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Lesson createLesson(LocalDateTime time, Duration dur, Instructor instructor) {
        Lesson l = new Lesson(time, dur, instructor);
        lessonRegistry.add(l);
        saveLessons();
        return l;
    }

    public Lesson getLessonById(UUID id) {
        return lessonRegistry.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void saveLessons() {
        File file = new File(LESSONS_FILE);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(lessonRegistry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLessons() {
        File file = new File(LESSONS_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Lesson> loaded = (List<Lesson>) ois.readObject();
            lessonRegistry.clear();
            lessonRegistry.addAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}