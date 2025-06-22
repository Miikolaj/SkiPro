package com.example.skipro.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Represents a ski instructor employed by the resort.
 * <p>
 * An {@code Instructor} extends {@link Employee} with instructor-specific data such as
 * a qualification level, aggregated customer ratings, and the set of {@link Lesson}s
 * they conduct. Each instructor is uniquely identified by an immutable {@link UUID}.
 * </p>
 */
public class Instructor extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private  UUID id = UUID.randomUUID();
    private  String qualificationLevel;
    private  List<Integer> ratings = new ArrayList<>();
    private Set<Lesson> lessons = new HashSet<>();

    /**
     * Constructs an {@code Instructor} with the provided personal data, experience, and qualification.
     *
     * @param yearsOfExperience total years of teaching or skiing experience
     * @param birthDate         date of birth
     * @param lastName          instructor’s last name
     * @param firstName         instructor’s first name
     * @param qualificationLevel certification/qualification level string
     */
    public Instructor(int yearsOfExperience, LocalDate birthDate, String lastName, String firstName, String qualificationLevel) {
        super(yearsOfExperience, birthDate, lastName, firstName);
        this.qualificationLevel = qualificationLevel;
    }

    public Instructor() {
        super();
        this.id = UUID.randomUUID();
        this.ratings = new ArrayList<>();
        this.lessons = new HashSet<>();
    }

    public String getQualificationLevel() {
        return qualificationLevel;
    }

    public List<Integer> getRatings() {
        return Collections.unmodifiableList(ratings);
    }

    public void addRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        ratings.add(rating);
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

    public void setQualificationLevel(String qualificationLevel) {
        this.qualificationLevel = qualificationLevel;
    }

    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings == null ? new ArrayList<>() : ratings;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons == null ? new HashSet<>() : lessons;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String getRole() {
        return "";
    }

    @Override
    public double calculateSalary() {
        double base = 3000.0;
        double experienceBonus = yearsOfExperience * 150.0;
        double averageRating = ratings.isEmpty() ? 0.0 :
                ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double ratingBonus = (averageRating - 3) * 200.0;
        return base + experienceBonus + ratingBonus;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + id +
                ", qualificationLevel='" + qualificationLevel + '\'' +
                ", ratings=" + ratings +
                // do not include lessons
                '}';
    }
}