package com.example.skipro.service;

import com.example.skipro.model.Client;
import com.example.skipro.model.Instructor;
import com.example.skipro.model.Lesson;
import com.example.skipro.model.enums.LessonStatus;
import com.example.skipro.util.PersistenceManager;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.time.Duration;

/**
 * Service responsible for creating, managing, and persisting ski lessons.
 */
@Service
public class LessonService {
    /**
     * Name of the file used for saving lessons.
     */
    private final PersistenceManager<Lesson> persistence = new PersistenceManager<>("src/main/java/com/example/skipro/data/lessons.ser");
    private  List<Lesson> lessonRegistry = new ArrayList<>();
    private final ClientService clientService = new ClientService();

    /**
     * Constructs a LessonService and loads lessons from file.
     */
    public LessonService() {
        lessonRegistry = persistence.load();
    }

    /**
     * Enrolls a client in the specified lesson.
     *
     * @param lessonId the lesson identifier
     * @param clientId the client identifier
     * @return {@code true} if the enrollment succeeded; {@code false} otherwise
     */
    public boolean enrollClientToLesson(UUID lessonId, UUID clientId) {
        Lesson lesson = getLessonById(lessonId);
        if (lesson == null) return false;
        Client client = clientService.getClientById(clientId);
        if (client == null) return false;

        if (lesson.getClients().stream().anyMatch(c -> c.getId().equals(clientId))) {
            return false;
        }

        try {
            lesson.enrollClient(client);
            persistence.save(lessonRegistry);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Creates a new lesson, adds it to the registry, persists the change, and returns it.
     *
     * @param time       the start time of the lesson
     * @param dur        the duration of the lesson
     * @param instructor the instructor leading the lesson
     */
    public void createLesson(LocalDateTime time, Duration dur, Instructor instructor) {
        Lesson l = new Lesson(time, dur, instructor);
        lessonRegistry.add(l);
        persistence.save(lessonRegistry);
    }

    /**
     * Removes a client from the specified lesson, persists the change, and returns the result.
     *
     * @param lessonId the lesson identifier
     * @param clientId the client identifier
     * @return {@code true} if the removal succeeded; {@code false} otherwise
     */
    public boolean removeClientFromLesson(UUID lessonId, UUID clientId) {
        System.out.println("Removing client " + clientId + " from lesson " + lessonId);
        Lesson lesson = getLessonById(lessonId);
        if (lesson == null) return false;
        Client client = clientService.getClientById(clientId);
        if (client == null) return false;

        lesson.cancelEnrollment(client);
        persistence.save(lessonRegistry);
        return true;
    }

    /**
     * Retrieves the list of planned lessons that do not contain the specified client.
     *
     * @param clientId the client identifier
     * @return list of planned lessons without the client
     */
    public List<Lesson> getPlannedLessonsWithoutClient(UUID clientId) {
        return lessonRegistry.stream()
                .filter(l -> l.getStatus() == LessonStatus.PLANNED)
                .filter(l -> l.getClients().stream()
                        .noneMatch(c -> c.getId().equals(clientId)))
                .toList();
    }

    /**
     * Retrieves the list of planned lessons for a given client.
     *
     * @param clientId the client identifier
     * @return list of planned lessons containing the client
     */
    public List<Lesson> getPlannedLessonsForClient(UUID clientId) {
        return lessonRegistry.stream()
                .filter(l -> l.getClients().stream()
                        .anyMatch(c -> c.getId().equals(clientId)))
                .filter(l -> l.getStatus() == LessonStatus.PLANNED)
                .toList();
    }

    /**
     * Retrieves the list of finished lessons for a given client.
     *
     * @param clientId the client identifier
     * @return list of finished lessons containing the client
     */
    public List<Lesson> getFinishedLessonsForClient(UUID clientId) {
        return lessonRegistry.stream()
                .filter(l -> l.getClients().stream()
                        .anyMatch(c -> c.getId().equals(clientId)))
                .filter(l -> l.getStatus() == LessonStatus.FINISHED)
                .toList();
    }

    /**
     * Retrieves a lesson by its identifier.
     *
     * @param id the lesson identifier
     * @return the lesson if found; otherwise {@code null}
     */
    public Lesson getLessonById(UUID id) {
        return lessonRegistry.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}