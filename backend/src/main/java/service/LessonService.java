package service;

import model.Client;
import model.Instructor;
import model.Lesson;
import model.enums.LessonStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;

public class LessonService {
    private final List<Lesson> lessonRegistry = new ArrayList<>();

    public LessonService() {}

    public List<Lesson> getAvailableLessons(Client client) {
        return lessonRegistry.stream()
                .filter(l -> l.getStatus() == LessonStatus.PLANNED)
                .filter(l -> !l.getClients().contains(client))
                .toList();
    }

    public Lesson getLessonForClient(Client clinet) {
        return lessonRegistry.stream()
                .filter(l -> l.getClients().contains(clinet))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No lesson found for the client."));
    }

    public void enrollClient(Lesson lesson, Client client) {
        if(lesson.getStatus() != LessonStatus.PLANNED) {
            throw new IllegalArgumentException("Cannot enroll in a lesson that is not planned.");
        }
        if(lesson.getClients().contains(client)) {
            throw new IllegalArgumentException("Client is already enrolled in this lesson.");
        }
        lesson.enrollClient(client);
    }

    public void startLesson(Lesson lesson) {
        if (lesson.getStatus() != LessonStatus.PLANNED) {
            throw new IllegalArgumentException("Only planned lessons can be started.");
        }
        lesson.start();
    }

    public void finishLesson(Lesson lesson) {
        if(lesson.getStatus() != LessonStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Only lessons in progress can be finished.");
        }
        lesson.finish();
    }

    public void rateInstructor(Lesson lesson, Client client, int rating) {
        if(lesson.getStatus() != LessonStatus.FINISHED) {
            throw new IllegalArgumentException("Only finished lessons can be rated.");
        }
        if(!lesson.getClients().contains(client)){
            throw new IllegalArgumentException("Client must be enrolled in the lesson to rate the instructor.");
        }
        lesson.getInstructor().setRating(rating);
    }

    public List<Lesson> getLessonsForInstructor(Instructor instructor) {
        return lessonRegistry.stream()
                .filter(l -> l.getInstructor().equals(instructor))
                .toList();
    }

    public Lesson createLesson(LocalDateTime time, Duration dur, Instructor instructor) {
        Lesson l = new Lesson(time, dur, instructor);
        lessonRegistry.add(l);
        return l;
    }
}