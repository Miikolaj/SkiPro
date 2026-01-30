package com.example.skipro.config;

import com.example.skipro.model.*;
import com.example.skipro.model.enums.Experience;
import com.example.skipro.model.enums.Status;
import com.example.skipro.model.enums.TrackDifficulty;
import com.example.skipro.repository.*;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class SampleDataSeeder implements ApplicationRunner {
    private final ClientRepository clientRepository;
    private final InstructorRepository instructorRepository;
    private final LessonRepository lessonRepository;

    // extra repositories so we can seed every table
    private final ResortRepository resortRepository;
    private final TrackRepository trackRepository;
    private final RescueTeamRepository rescueTeamRepository;
    private final RescueWorkerRepository rescueWorkerRepository;
    private final RescueWorkerTeamAssignmentRepository rescueWorkerTeamAssignmentRepository;
    private final EmploymentRepository employmentRepository;
    private final EquipmentRepository equipmentRepository;
    private final RentalClerkRepository rentalClerkRepository;
    private final RentalRepository rentalRepository;

    public SampleDataSeeder(
            ClientRepository clientRepository,
            InstructorRepository instructorRepository,
            LessonRepository lessonRepository,
            ResortRepository resortRepository,
            TrackRepository trackRepository,
            RescueTeamRepository rescueTeamRepository,
            RescueWorkerRepository rescueWorkerRepository,
            RescueWorkerTeamAssignmentRepository rescueWorkerTeamAssignmentRepository,
            EmploymentRepository employmentRepository,
            EquipmentRepository equipmentRepository,
            RentalClerkRepository rentalClerkRepository,
            RentalRepository rentalRepository
    ) {
        this.clientRepository = clientRepository;
        this.instructorRepository = instructorRepository;
        this.lessonRepository = lessonRepository;
        this.resortRepository = resortRepository;
        this.trackRepository = trackRepository;
        this.rescueTeamRepository = rescueTeamRepository;
        this.rescueWorkerRepository = rescueWorkerRepository;
        this.rescueWorkerTeamAssignmentRepository = rescueWorkerTeamAssignmentRepository;
        this.employmentRepository = employmentRepository;
        this.equipmentRepository = equipmentRepository;
        this.rentalClerkRepository = rentalClerkRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Keep it idempotent: only seed if DB is empty
        if (clientRepository.count() > 0
                || instructorRepository.count() > 0
                || lessonRepository.count() > 0
                || resortRepository.count() > 0
                || trackRepository.count() > 0
                || rescueTeamRepository.count() > 0
                || rescueWorkerRepository.count() > 0
                || employmentRepository.count() > 0
                || equipmentRepository.count() > 0
                || rentalClerkRepository.count() > 0
                || rentalRepository.count() > 0
        ) {
            return;
        }

        // --- Resort + Tracks ---
        Resort resort = new Resort("SkiPro Resort", "Zakopane");
        resortRepository.save(resort);

        Track trackBlue = resort.createTrack("Blue Run", TrackDifficulty.BLUE, 2.4);
        Track trackRed = resort.createTrack("Red Run", TrackDifficulty.RED, 3.6);
        trackRepository.save(trackBlue);
        trackRepository.save(trackRed);

        // --- Employees ---
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

        RentalClerk clerk = new RentalClerk(4, LocalDate.of(1998, 4, 2), "Nowicki", "Patryk", 0);
        rentalClerkRepository.save(clerk);

        RescueWorker rw1 = new RescueWorker(7, LocalDate.of(1992, 1, 12), "Malinowski", "Kamil", "LIC-1001",
                List.of("First Aid", "Evacuation"));
        RescueWorker rw2 = new RescueWorker(3, LocalDate.of(2000, 9, 30), "Sikora", "Bartek", "LIC-1002",
                List.of("First Aid"));
        rescueWorkerRepository.save(rw1);
        rescueWorkerRepository.save(rw2);

        // --- Employments (association-class 1-*) ---
        // Instructor employed at resort
        Employment e1 = new Employment(resort, i1, LocalDate.now().minusYears(2));
        Employment e2 = new Employment(resort, i2, LocalDate.now().minusYears(1));
        Employment e3 = new Employment(resort, i3, LocalDate.now().minusYears(4));
        Employment e4 = new Employment(resort, clerk, LocalDate.now().minusMonths(8));
        Employment e5 = new Employment(resort, rw1, LocalDate.now().minusYears(3));
        Employment e6 = new Employment(resort, rw2, LocalDate.now().minusMonths(10));

        employmentRepository.save(e1);
        employmentRepository.save(e2);
        employmentRepository.save(e3);
        employmentRepository.save(e4);
        employmentRepository.save(e5);
        employmentRepository.save(e6);

        // --- Rescue Teams + assignment history (pkt 12) ---
        RescueTeam teamA = new RescueTeam("Team A", trackBlue, "CH-1", List.of("Defibrillator"), Status.ACTIVE);
        RescueTeam teamB = new RescueTeam("Team B", trackRed, "CH-2", List.of("Radio", "Sled"), Status.ON_RESERVE);
        rescueTeamRepository.save(teamA);
        rescueTeamRepository.save(teamB);

        // rw1: history - was in teamB, now in teamA
        RescueWorkerTeamAssignment rw1Old = new RescueWorkerTeamAssignment(rw1, teamB, LocalDate.now().minusMonths(6));
        rw1Old.end(LocalDate.now().minusMonths(2));
        RescueWorkerTeamAssignment rw1Current = new RescueWorkerTeamAssignment(rw1, teamA, LocalDate.now().minusMonths(2));

        // rw2: current in teamB
        RescueWorkerTeamAssignment rw2Current = new RescueWorkerTeamAssignment(rw2, teamB, LocalDate.now().minusMonths(1));

        rescueWorkerTeamAssignmentRepository.save(rw1Old);
        rescueWorkerTeamAssignmentRepository.save(rw1Current);
        rescueWorkerTeamAssignmentRepository.save(rw2Current);

        // --- Clients ---
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        Client c1 = new Client("Alex", "Kowalski", LocalDate.of(2003, 1, 10), Experience.BEGINNER, encoder.encode("pass123"));
        Client c2 = new Client("Maja", "Nowak", LocalDate.of(1997, 6, 5), Experience.EXPERIENCED, encoder.encode("pass123"));
        Client c3 = new Client("Ola", "Zielinska", LocalDate.of(1989, 3, 21), Experience.ADVANCED, encoder.encode("pass123"));

        clientRepository.save(c1);
        clientRepository.save(c2);
        clientRepository.save(c3);

        // --- Equipment + Rental (history for client + rentalsHandled for clerk) ---
        Equipment eq1 = new Equipment("Skis", "170cm", "GOOD", 120);
        Equipment eq2 = new Equipment("Helmet", "L", "NEW", 40);
        equipmentRepository.save(eq1);
        equipmentRepository.save(eq2);

        // One active rental (eq1 -> Alex), processed by clerk
        eq1.setInUse(true);
        Rental rental1 = new Rental(eq1, c1, clerk);
        rental1.setPlannedReturnDate(LocalDateTime.now().plusDays(2));
        clerk.incrementRentalsHandled();
        rentalRepository.save(rental1);

        // One returned rental (eq2 -> Maja), processed by clerk
        eq2.setInUse(true);
        Rental rental2 = new Rental(eq2, c2, clerk);
        rental2.setPlannedReturnDate(LocalDateTime.now().plusDays(1));
        rental2.returnEquipment();
        clerk.incrementRentalsHandled();
        rentalRepository.save(rental2);

        rentalClerkRepository.save(clerk);
        equipmentRepository.save(eq1);
        equipmentRepository.save(eq2);

        // --- Lessons ---
        Lesson l1 = new Lesson(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0), Duration.ofMinutes(60), i1);
        Lesson l2 = new Lesson(LocalDateTime.now().plusDays(2).withHour(12).withMinute(0).withSecond(0).withNano(0), Duration.ofMinutes(90), i2);
        Lesson l3 = new Lesson(LocalDateTime.now().plusDays(3).withHour(9).withMinute(30).withSecond(0).withNano(0), Duration.ofMinutes(120), i3);

        // Capacity variants for testing
        Lesson lCap2 = new Lesson(LocalDateTime.now().plusDays(4).withHour(9).withMinute(0).withSecond(0).withNano(0), Duration.ofMinutes(60), i1, 2);
        Lesson lCap4 = new Lesson(LocalDateTime.now().plusDays(4).withHour(13).withMinute(0).withSecond(0).withNano(0), Duration.ofMinutes(90), i2, 4);

        // A "full" lesson for testing enrollment failures (capacity=1) -> will be filled to full
        Lesson lFull = new Lesson(LocalDateTime.now().plusDays(4).withHour(16).withMinute(0).withSecond(0).withNano(0), Duration.ofMinutes(60), i3, 1);

        // Another full lesson (capacity=2) -> will be filled to full
        Lesson lFull2 = new Lesson(LocalDateTime.now().plusDays(5).withHour(11).withMinute(0).withSecond(0).withNano(0), Duration.ofMinutes(60), i3, 2);

        // finished lessons for Alex so "Finished Lessons" view has data
        Lesson l4 = new Lesson(LocalDateTime.now().minusDays(3).withHour(11).withMinute(0).withSecond(0).withNano(0), Duration.ofMinutes(60), i1);
        l4.enrollClient(c1);
        l4.start();
        l4.finish();

        Lesson l5 = new Lesson(LocalDateTime.now().minusDays(1).withHour(14).withMinute(0).withSecond(0).withNano(0), Duration.ofMinutes(90), i2);
        l5.enrollClient(c1);
        l5.start();
        l5.finish();

        // pre-enroll some clients so GUI has all 3 lists meaningful
        l1.enrollClient(c1);
        l2.enrollClient(c2);

        // Fill the limited-capacity lessons to full
        lFull.enrollClient(c2);            // capacity=1 -> FULL
        lFull2.enrollClient(c2);           // capacity=2
        lFull2.enrollClient(c3);           // capacity=2 -> FULL

        // Partially fill some other capacity variants
        lCap2.enrollClient(c3);            // 1/2
        lCap4.enrollClient(c1);            // 1/4
        lCap4.enrollClient(c2);            // 2/4

        lessonRepository.save(l1);
        lessonRepository.save(l2);
        lessonRepository.save(l3);
        lessonRepository.save(lCap2);
        lessonRepository.save(lCap4);
        lessonRepository.save(lFull);
        lessonRepository.save(lFull2);
        lessonRepository.save(l4);
        lessonRepository.save(l5);
    }
}
