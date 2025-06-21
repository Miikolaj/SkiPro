package com.example.skipro.model;

import java.time.LocalDate;
import java.util.*;

public class Instructor extends Employee {
    private final UUID id = UUID.randomUUID();
    private final String qualificationLevel;
    private Optional<Integer> rating = Optional.empty();
    private final Set<Lesson> lessons = new HashSet<>();

    public Instructor(int yearsOfExperience, LocalDate birthDate, String lastName, String firstName, String qualificationLevel) {
        super(yearsOfExperience, birthDate, lastName, firstName);
        this.qualificationLevel = qualificationLevel;
    }

    public String getQualificationLevel() {
        return qualificationLevel;
    }

    public Optional<Integer> getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = Optional.of(rating);
    }

    void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
    }

    public Set<Lesson> getLessons() {
        return Collections.unmodifiableSet(lessons);
    }

    @Override
    public String getRole() {
        return "";
    }

    @Override
    public double calculateSalary() {
        double base = 3000.0;
        double experienceBonus = yearsOfExperience * 150.0;
        double ratingBonus = rating.map(r -> (r - 3) * 200.0).orElse(0.0);
        return base + experienceBonus + ratingBonus;
    }
}