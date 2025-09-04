package com.example.skipro.model;

import com.example.skipro.model.enums.LessonStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a ski lesson conducted by a single {@link Instructor} and attended by zero or more {@link Client}s.
 * <p>
 * Each lesson has a unique immutable {@link UUID}, a scheduled start {@link #dateTime}, a {@link #duration},
 * and a mutable {@link #status}. The attending clients are stored in an internal {@link Set} to avoid duplicates.
 * </p>
 */
public class Lesson implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id = UUID.randomUUID();
    private final LocalDateTime dateTime;
    private final Duration duration;
    private LessonStatus status;
    private final Instructor instructor;
    private final Set<Client> clients = new HashSet<>();

    /**
     * Constructs a new {@code Lesson} with the specified schedule, duration, and instructor.
     * The lesson is initially in the {@link LessonStatus#PLANNED} state and automatically
     * registers itself with the instructor.
     *
     * @param dateTime   lesson start date and time (non-null)
     * @param duration   lesson duration (non-null)
     * @param instructor instructor conducting the lesson (non-null)
     * @throws IllegalArgumentException if any argument is {@code null}
     */
    public Lesson(LocalDateTime dateTime, Duration duration, Instructor instructor) {
        if (dateTime == null || duration == null || instructor == null) {
            throw new IllegalArgumentException("Lesson data and instructor must not be null");
        }
        this.dateTime = dateTime;
        this.duration = duration;
        this.instructor = instructor;
        this.status = LessonStatus.PLANNED;

        instructor.addLesson(this);
    }

    /** Enroll a client to the lesson (if not already enrolled) */
    public void enrollClient(Client client) {
        if (status != LessonStatus.PLANNED)
            throw new IllegalStateException("Only planned lessons allow enrollment.");
        if (clients.add(client)) {
            client.addLesson(this);
        }
    }

    /** Remove a client from the lesson */
    public void cancelEnrollment(Client client) {
        if (clients.remove(client)) {
            client.removeLesson(this);
        }
    }

    /** Start the lesson */
    public void start() {
        if (status != LessonStatus.PLANNED)
            throw new IllegalStateException("Only planned lessons can be started.");
        status = LessonStatus.IN_PROGRESS;
    }

    /** Finish the lesson */
    public void finish() {
        if (status != LessonStatus.IN_PROGRESS)
            throw new IllegalStateException("Only lessons in progress can be finished.");
        status = LessonStatus.FINISHED;
    }

    /** Cancel the lesson */
    public void cancel() {
        if (status == LessonStatus.FINISHED)
            throw new IllegalStateException("Finished lessons cannot be cancelled.");
        status = LessonStatus.CANCELLED;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LessonStatus getStatus() {
        return status;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public Set<Client> getClients() {
        return Collections.unmodifiableSet(clients);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", duration=" + duration +
                ", status=" + status +
                ", instructor=" + instructor +
                ", clients=" + clients +
                '}';
    }
}