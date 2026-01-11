package com.example.skipro.config;

import com.example.skipro.model.Client;
import com.example.skipro.model.Instructor;
import com.example.skipro.model.Lesson;
import com.example.skipro.model.enums.Experience;
import com.example.skipro.repository.ClientRepository;
import com.example.skipro.repository.InstructorRepository;
import com.example.skipro.repository.LessonRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class SampleDataSeeder implements ApplicationRunner {
    private final ClientRepository clientRepository;
    private final InstructorRepository instructorRepository;
    private final LessonRepository lessonRepository;

    public SampleDataSeeder(ClientRepository clientRepository,
                            InstructorRepository instructorRepository,
                            LessonRepository lessonRepository) {
        this.clientRepository = clientRepository;
        this.instructorRepository = instructorRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Keep it idempotent: only seed if DB is empty
        if (clientRepository.count() > 0 || instructorRepository.count() > 0 || lessonRepository.count() > 0) {
            return;
        }

        Instructor i1 = new Instructor(10, LocalDate.of(1985, 5, 20), "Smith", "John", "Level 3");
        i1.addRating(5);
        i1.addRating(4);

        Instructor i2 = new Instructor(5, LocalDate.of(1990, 8, 15), "Doe", "Jane", "Level 2");
        i2.addRating(4);

        Instructor i3 = new Instructor(15, LocalDate.of(1978, 12, 5), "Brown", "Emily", "Level 4");
        i3.addRating(5);
        i3.addRating(5);

        instructorRepository.save(i1);
        instructorRepository.save(i2);
        instructorRepository.save(i3);

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        Client c1 = new Client("Alex", "Kowalski", 22, Experience.BEGINNER, encoder.encode("pass123"));
        Client c2 = new Client("Maja", "Nowak", 28, Experience.INTERMEDIATE, encoder.encode("pass123"));
        Client c3 = new Client("Ola", "Zielinska", 35, Experience.ADVANCED, encoder.encode("pass123"));

        clientRepository.save(c1);
        clientRepository.save(c2);
        clientRepository.save(c3);

        Lesson l1 = new Lesson(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0), Duration.ofMinutes(60), i1);
        Lesson l2 = new Lesson(LocalDateTime.now().plusDays(2).withHour(12).withMinute(0).withSecond(0).withNano(0), Duration.ofMinutes(90), i2);
        Lesson l3 = new Lesson(LocalDateTime.now().plusDays(3).withHour(9).withMinute(30).withSecond(0).withNano(0), Duration.ofMinutes(120), i3);

        // pre-enroll some clients so GUI has all 3 lists meaningful
        l1.enrollClient(c1);
        l2.enrollClient(c2);

        lessonRepository.save(l1);
        lessonRepository.save(l2);
        lessonRepository.save(l3);
    }
}
