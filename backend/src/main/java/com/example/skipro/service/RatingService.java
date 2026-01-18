package com.example.skipro.service;

import com.example.skipro.model.Client;
import com.example.skipro.model.Lesson;
import com.example.skipro.model.LessonRating;
import com.example.skipro.model.enums.LessonStatus;
import com.example.skipro.repository.ClientRepository;
import com.example.skipro.repository.LessonRatingRepository;
import com.example.skipro.repository.LessonRepository;
import com.example.skipro.repository.InstructorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * Use-case service: rate instructor after finished lesson.
 */
@Service
public class RatingService {
    private final LessonRepository lessonRepository;
    private final ClientRepository clientRepository;
    private final InstructorRepository instructorRepository;
    private final LessonRatingRepository lessonRatingRepository;

    public RatingService(
            LessonRepository lessonRepository,
            ClientRepository clientRepository,
            InstructorRepository instructorRepository,
            LessonRatingRepository lessonRatingRepository
    ) {
        this.lessonRepository = lessonRepository;
        this.clientRepository = clientRepository;
        this.instructorRepository = instructorRepository;
        this.lessonRatingRepository = lessonRatingRepository;
    }

    /**
     * @return true if rating was saved; false if validation failed.
     */
    @Transactional
    public boolean rateInstructor(UUID lessonId, UUID clientId, int rating) {
        if (lessonId == null || clientId == null) return false;
        if (rating < 1 || rating > 5) return false;

        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
        if (lesson == null) return false;

        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) return false;

        // Only after FINISHED
        if (lesson.getStatus() != LessonStatus.FINISHED) return false;

        // Only participants can rate
        boolean participated = lesson.getClients().stream().anyMatch(c -> Objects.equals(c.getId(), clientId));
        if (!participated) return false;

        // One rating per (lesson, client)
        if (lessonRatingRepository.existsByLesson_IdAndClient_Id(lessonId, clientId)) {
            return false;
        }

        // Instructor exists / is consistent
        var instructor = lesson.getInstructor();
        if (instructor == null) return false;
        // Ensure instructor is managed/persistent (optional but safe)
        if (instructor.getId() == null || !instructorRepository.existsById(instructor.getId())) {
            return false;
        }

        // Persist rating entity
        LessonRating lr = new LessonRating(lesson, client, instructor, rating);
        lessonRatingRepository.save(lr);

        // Add to instructor aggregated ratings (stored in ElementCollection)
        instructor.addRating(rating);
        instructorRepository.save(instructor);

        return true;
    }
}


