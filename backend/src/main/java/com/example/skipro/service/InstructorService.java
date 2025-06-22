package com.example.skipro.service;

import com.example.skipro.model.Instructor;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class InstructorService {
    private static final String INSTRUCTORS_FILE = "src/main/java/com/example/skipro/data/instructors.ser";
    private final List<Instructor> instructorRegistry = new ArrayList<>();

    public InstructorService() {
        loadInstructors();
    }

    public List<Instructor> getAllInstructors() {
        return new ArrayList<>(instructorRegistry);
    }

    public Instructor getInstructorById(String id) {
        return instructorRegistry.stream()
                .filter(i -> String.valueOf(i.getId()).equals(id))
                .findFirst()
                .orElse(null);
    }

    public void saveInstructors() {
        File file = new File(INSTRUCTORS_FILE);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(instructorRegistry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadInstructors() {
        File file = new File(INSTRUCTORS_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Instructor> loaded = (List<Instructor>) ois.readObject();
            instructorRegistry.clear();
            instructorRegistry.addAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createMockupData() {

        Instructor instructor1 = new Instructor(
                5,
                LocalDate.of(1985, 3, 15),
                "Smith",
                "John",
                "Level 2"
        );
        instructor1.setRatings(Arrays.asList(5, 4, 5));
        instructor1.setLessons(new HashSet<>());

        Instructor instructor2 = new Instructor(
                8,
                LocalDate.of(1978, 7, 22),
                "Doe",
                "Jane",
                "Level 3"
        );
        instructor2.setRatings(Arrays.asList(4, 4, 5));
        instructor2.setLessons(new HashSet<>());
        instructorRegistry.addAll(Arrays.asList(instructor1, instructor2));
    }

    public void printAllInstructors() {
        for (Instructor instructor : instructorRegistry) {
            System.out.println("ID: " + instructor.getId());
            System.out.println("Name: " + instructor.getFirstName() + " " + instructor.getLastName());
            System.out.println("Birth Date: " + instructor.getBirthDate());
            System.out.println("Years of Experience: " + instructor.getYearsOfExperience());
            System.out.println("Qualification Level: " + instructor.getQualificationLevel());
            System.out.println("Ratings: " + instructor.getRatings());
            System.out.println("Lessons: " + instructor.getLessons());
            System.out.println("-----");
        }
    }
}