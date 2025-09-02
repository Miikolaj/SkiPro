package com.example.skipro.service;

import com.example.skipro.model.Client;
import com.example.skipro.model.Instructor;
import com.example.skipro.model.Lesson;
import com.example.skipro.model.enums.LessonStatus;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.time.Duration;

/**
 * Service responsible for creating, managing, and persisting ski lessons.
 */
public class LessonService {
    /**
     * Name of the file used for saving lessons.
     */
    private static final String LESSONS_FILE = "src/main/java/com/example/skipro/data/lessons.ser"; //Registry containing all lessons.
    private final List<Lesson> lessonRegistry = new ArrayList<>(); // Registry containing all lessons.
    private final ClientService clientService = new ClientService(); // Service used for looking up clients when enrolling them in a lesson.

    /**
     * Constructs a LessonService and loads lessons from file.
     */
    public LessonService() {
        loadLessons();
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
            System.out.println("Client already enrolled in lesson");
            return false;
        }

        try {
            lesson.enrollClient(client);
            saveLessons();
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
     * @return the newly created lesson
     */
    public Lesson createLesson(LocalDateTime time, Duration dur, Instructor instructor) {
        Lesson l = new Lesson(time, dur, instructor);
        lessonRegistry.add(l);
        saveLessons();
        return l;
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

    /**
     * Saves the current lesson registry to a file. Necessary directories are created automatically.
     */
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

    /**
     * Loads lessons from the persistent file into memory. If the file does not exist, the method returns silently.
     */
    public void loadLessons() {
        File file = new File(LESSONS_FILE);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Lesson> loaded = (List<Lesson>) ois.readObject();
            lessonRegistry.clear();
            lessonRegistry.addAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean removeClientFromLesson(UUID lessonId, UUID clientId) {
        Lesson lesson = getLessonById(lessonId);
        if (lesson == null) return false;
        Client client = clientService.getClientById(clientId);
        if (client == null) return false;

        lesson.cancelEnrollment(client);
        saveLessons();
        return true;
    }
}