package com.example.skipro.service;

import com.example.skipro.model.Client;
import com.example.skipro.model.Instructor;
import com.example.skipro.model.Lesson;
import com.example.skipro.model.enums.LessonStatus;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.time.Duration;

public class LessonService {
    private static final String LESSONS_FILE = "src/main/java/com/example/skipro/data/lessons.ser";
    private final List<Lesson> lessonRegistry = new ArrayList<>();
    private final ClientService clientService = new ClientService();
    private final InstructorService instructorService = new InstructorService();

    public LessonService() {
        loadLessons();
    }

    public List<Lesson> getPlannedLessonsWithoutClient(UUID clientId) {
        return lessonRegistry.stream()
                .filter(l -> l.getStatus() == LessonStatus.PLANNED)
                .filter(l -> l.getClients().stream()
                        .noneMatch(c -> c.getId().equals(clientId)))
                .toList();
    }

    public List<Lesson> getPlannedLessonsForClient(UUID clientId) {
        return lessonRegistry.stream()
                .filter(l -> l.getClients().stream()
                        .anyMatch(c -> c.getId().equals(clientId)))
                .filter(l -> l.getStatus() == LessonStatus.PLANNED)
                .toList();
    }

    public List<Lesson> getFinishedLessonsForClient(UUID clientId) {
        return lessonRegistry.stream()
                .filter(l -> l.getClients().stream()
                        .anyMatch(c -> c.getId().equals(clientId)))
                .filter(l -> l.getStatus() == LessonStatus.FINISHED)
                .toList();
    }

    public Lesson createLesson(LocalDateTime time, Duration dur, Instructor instructor) {
        Lesson l = new Lesson(time, dur, instructor);
        lessonRegistry.add(l);
        saveLessons();
        return l;
    }

    public void saveLessons() {
        File file = new File(LESSONS_FILE);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(lessonRegistry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLessons() {
        File file = new File(LESSONS_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Lesson> loaded = (List<Lesson>) ois.readObject();
            lessonRegistry.clear();
            lessonRegistry.addAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createMockupData() {
        Client client1 =  clientService.getClientById("d3ce3558-5e91-48ad-8b5c-3e76b488dfb0");
        Client client2 = clientService.getClientById("c59b8e61-a4af-4ff0-a61f-a92733aa9b76");
        Instructor instructor1 = instructorService.getInstructorById("8857f83d-7e5a-4125-8cb5-642f3379473d");
        Instructor instructor2 = instructorService.getInstructorById("b88921a2-0baf-4e7f-86ff-f60567b73f21");

        // 3 planned lessons, no clients
        Lesson lesson1 = new Lesson(LocalDate.now().atStartOfDay(), Duration.ofMinutes(150),  instructor1);
        Lesson lesson2 = new Lesson(LocalDate.now().atStartOfDay(), Duration.ofMinutes(100),  instructor2);
        Lesson lesson3 = new Lesson(LocalDate.now().atStartOfDay(),  Duration.ofMinutes(60), instructor1);

        lesson1.enrollClient(client1);


        Lesson lesson4 = new Lesson(LocalDate.now().atStartOfDay(),  Duration.ofMinutes(60), instructor1);
        Lesson lesson5 = new Lesson(LocalDate.now().atStartOfDay(),  Duration.ofMinutes(60), instructor1);
        Lesson lesson6 = new Lesson(LocalDate.now().atStartOfDay(),  Duration.ofMinutes(60), instructor1);
        Lesson lesson7 = new Lesson(LocalDate.now().atStartOfDay(),  Duration.ofMinutes(60), instructor1);

        lesson4.enrollClient(client2);
        lesson5.enrollClient(client2);
        lesson6.enrollClient(client2);
        lesson7.enrollClient(client2);


        Lesson lesson8 = new Lesson(LocalDate.now().atStartOfDay(),  Duration.ofMinutes(60), instructor1);
        Lesson lesson9 = new Lesson(LocalDate.now().atStartOfDay(),  Duration.ofMinutes(60), instructor1);

        lesson8.enrollClient(client1);
        lesson9.enrollClient(client1);

        lesson8.start();
        lesson9.start();
        lesson8.finish();
        lesson9.finish();

        lessonRegistry.addAll(Arrays.asList(lesson1, lesson2, lesson3, lesson4, lesson5, lesson6, lesson7, lesson8, lesson9));
    }


}