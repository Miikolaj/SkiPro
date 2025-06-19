import model.Client;
import model.Instructor;
import model.Lesson;
import model.enums.Experience;
import service.LessonService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Instructor instructor = new Instructor(5, LocalDate.of(1990, 1, 1), "Doe", "John", "Advanced");

        Client client = new Client("Mikolaj", "Kowalski", 18,   Experience.BEGINNER);

        LessonService lessonService = new LessonService();

        Lesson lesson = lessonService.createLesson(LocalDateTime.of(2025,3,5,10,0), Duration.ofHours(2), instructor);

        System.out.println("Lesson created");
        System.out.println("Lesson: " + lesson);

        System.out.println(lessonService.getAvailableLessons(client));

        try {
            lessonService.enrollClient(lesson, client);
            System.out.println("Client enrolled in lesson");
        } catch (Exception e) {
            System.out.println("Error enrolling client: " + e.getMessage());
        }

        lessonService.startLesson(lessonService.getLessonForClient(client));

        System.out.println(lessonService.getLessonForClient(client));

        lessonService.finishLesson(lessonService.getLessonForClient(client));

        System.out.println(lessonService.getLessonForClient(client));

        lessonService.rateInstructor(lessonService.getLessonForClient(client), client,5);
        System.out.println("Instructor rating: " + instructor.getRating().orElse(0));
    }
}
