package com.example.skipro.repository;

import com.example.skipro.model.LessonRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LessonRatingRepository extends JpaRepository<LessonRating, UUID> {
    boolean existsByLesson_IdAndClient_Id(UUID lessonId, UUID clientId);
}

