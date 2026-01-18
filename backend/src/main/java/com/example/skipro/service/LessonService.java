package com.example.skipro.service;

import com.example.skipro.model.Client;
import com.example.skipro.model.Instructor;
import com.example.skipro.model.Lesson;
import com.example.skipro.model.enums.LessonStatus;
import com.example.skipro.repository.ClientRepository;
import com.example.skipro.repository.LessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service responsible for creating and managing ski lessons.
 */
@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final ClientRepository clientRepository;

    public LessonService(LessonRepository lessonRepository, ClientRepository clientRepository) {
        this.lessonRepository = lessonRepository;
        this.clientRepository = clientRepository;
    }

    public Client getClientById(UUID id) {
        return id == null ? null : clientRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean enrollClientToLesson(UUID lessonId, UUID clientId) {
        if (lessonId == null || clientId == null) return false;

        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
        if (lesson == null) return false;

        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) return false;

        if (lesson.getClients().stream().anyMatch(c -> c.getId().equals(clientId))) {
            return false;
        }

        try {
            lesson.enrollClient(client);
            lessonRepository.save(lesson);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public void createLesson(LocalDateTime time, Duration dur, Instructor instructor) {
        Lesson l = new Lesson(time, dur, instructor);
        lessonRepository.save(l);
    }

    @Transactional
    public void createLesson(LocalDateTime time, Duration dur, Instructor instructor, Integer capacity) {
        if (capacity == null) {
            createLesson(time, dur, instructor);
            return;
        }
        Lesson l = new Lesson(time, dur, instructor, capacity);
        lessonRepository.save(l);
    }

    @Transactional
    public boolean removeClientFromLesson(UUID lessonId, UUID clientId) {
        if (lessonId == null || clientId == null) return false;

        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
        if (lesson == null) return false;

        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) return false;

        try {
            lesson.cancelEnrollment(client);
            lessonRepository.save(lesson);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Available lessons: PLANNED lessons where the client is NOT enrolled.
     * Implemented without custom SQL/JPQL queries: we load lessons and filter via the association.
     */
    @Transactional(readOnly = true)
    public List<Lesson> getPlannedLessonsWithoutClient(UUID clientId) {
        if (clientId == null) return List.of();

        // this also validates that the client exists
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) return List.of();

        return lessonRepository.findAll().stream()
                .filter(l -> l.getStatus() == LessonStatus.PLANNED)
                .filter(l -> l.getClients().stream().noneMatch(c -> c.getId().equals(clientId)))
                .toList();
    }

    /**
     * Enrolled lessons: lessons with a given status where the client IS enrolled.
     */
    @Transactional(readOnly = true)
    public List<Lesson> getLessonsForClientByStatus(UUID clientId, LessonStatus status) {
        if (clientId == null || status == null) return List.of();

        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) return List.of();

        // Walk the association from Client -> lessons (shows association in action).
        return client.getLessons().stream()
                .filter(l -> l.getStatus() == status)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Lesson> getPlannedLessonsForClient(UUID clientId) {
        return getLessonsForClientByStatus(clientId, LessonStatus.PLANNED);
    }

    @Transactional(readOnly = true)
    public List<Lesson> getFinishedLessonsForClient(UUID clientId) {
        return getLessonsForClientByStatus(clientId, LessonStatus.FINISHED);
    }

    @Transactional(readOnly = true)
    public Lesson getLessonById(UUID id) {
        return id == null ? null : lessonRepository.findById(id).orElse(null);
    }

    /**
     * Returns clients enrolled in a given lesson via the Lesson->clients association.
     */
    @Transactional(readOnly = true)
    public List<Client> getLessonClients(UUID lessonId) {
        if (lessonId == null) return List.of();
        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
        if (lesson == null) return List.of();
        return lesson.getClients().stream().toList();
    }

    /**
     * Counts clients enrolled in a lesson via the Lesson->clients association.
     */
    @Transactional(readOnly = true)
    public long countClients(UUID lessonId) {
        if (lessonId == null) return 0;
        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
        if (lesson == null) return 0;
        return lesson.getClients().size();
    }
}
