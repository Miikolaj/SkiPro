package com.example.skipro.config;

import com.example.skipro.model.Client;
import com.example.skipro.model.Instructor;
import com.example.skipro.model.Lesson;
import com.example.skipro.model.enums.Experience;
import com.example.skipro.repository.ClientRepository;
import com.example.skipro.repository.InstructorRepository;
import com.example.skipro.repository.LessonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * NOTE: Disabled in favor of {@link SampleDataSeeder}.
 *
 * Having two independent seeders causes non-deterministic DB contents at startup
 * (e.g., sometimes Alex.Kowalski doesn't exist, so /auth/login returns null).
 */
// @Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(
            ClientRepository clientRepository,
            InstructorRepository instructorRepository,
            LessonRepository lessonRepository
    ) {
        return args -> {
            if (clientRepository.count() > 0 || instructorRepository.count() > 0 || lessonRepository.count() > 0) {
                return;
            }

            PasswordEncoder encoder = new BCryptPasswordEncoder();

            // --- Clients ---
            Client c1 = new Client("Jan", "Kowalski", 22, Experience.BEGINNER, encoder.encode("pass123"));
            Client c2 = new Client("Anna", "Nowak", 27, Experience.INTERMEDIATE, encoder.encode("pass123"));
            Client c3 = new Client("Piotr", "Zielinski", 34, Experience.ADVANCED, encoder.encode("pass123"));
            clientRepository.save(c1);
            clientRepository.save(c2);
            clientRepository.save(c3);

            // --- Instructors ---
            Instructor i1 = new Instructor(6, LocalDate.of(1990, 5, 12), "Wojcik", "Marek", "PRO");
            i1.addRating(5);
            i1.addRating(4);

            Instructor i2 = new Instructor(3, LocalDate.of(1996, 2, 3), "Kaczmarek", "Kasia", "INTERMEDIATE");
            i2.addRating(4);

            Instructor i3 = new Instructor(10, LocalDate.of(1986, 11, 20), "Lewandowski", "Tomasz", "EXPERT");
            i3.addRating(5);
            i3.addRating(5);
            i3.addRating(4);

            instructorRepository.save(i1);
            instructorRepository.save(i2);
            instructorRepository.save(i3);

            // --- Lessons ---
            Lesson l1 = new Lesson(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0), Duration.ofMinutes(90), i1);
            Lesson l2 = new Lesson(LocalDateTime.now().plusDays(2).withHour(12).withMinute(0), Duration.ofMinutes(60), i2);
            Lesson l3 = new Lesson(LocalDateTime.now().plusDays(3).withHour(9).withMinute(30), Duration.ofMinutes(120), i3);

            // enroll some clients
            l2.enrollClient(c1);
            l3.enrollClient(c2);
            l3.enrollClient(c3);

            lessonRepository.save(l1);
            lessonRepository.save(l2);
            lessonRepository.save(l3);
        };
    }
}
