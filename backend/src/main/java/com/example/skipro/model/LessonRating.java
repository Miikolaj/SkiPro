package com.example.skipro.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a single rating given by a {@link Client} for an {@link Instructor} after a specific {@link Lesson}.
 * <p>
 * Business rules (enforced in service layer):
 * <ul>
 *   <li>Only lesson participants can rate.</li>
 *   <li>Rating is allowed only after the lesson is FINISHED.</li>
 *   <li>A client can rate a given lesson at most once (unique constraint).</li>
 * </ul>
 */
@Entity
@Table(
        name = "lesson_ratings",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_lesson_client_rating", columnNames = {"lesson_id", "client_id"})
        }
)
public class LessonRating {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected LessonRating() {
        // for JPA
    }

    public LessonRating(Lesson lesson, Client client, Instructor instructor, int rating) {
        if (lesson == null || client == null || instructor == null) {
            throw new IllegalArgumentException("Lesson, client and instructor must not be null");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.lesson = lesson;
        this.client = client;
        this.instructor = instructor;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Client getClient() {
        return client;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public int getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

