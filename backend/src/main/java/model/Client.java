package model;

import model.enums.Experience;

import java.util.*;

/**
 * Represents a ski resort client (customer) who can participate in lessons.
 */
public class Client {
    private final UUID id = UUID.randomUUID();
    private final String firstName;
    private final String lastName;
    private final int age;
    private final Experience experience;

    /**
     * Association: 0..* Lessons the client is enrolled in
     */
    private final Set<Lesson> lessons = new HashSet<>();
    private final Set<Rental> rentals = new HashSet<>();

    public Client(String firstName, String lastName, int age, Experience experience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.experience = experience;
    }

    void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
    }

    public Set<Lesson> getLessons() {
        return Collections.unmodifiableSet(lessons);
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public Experience getExperience() {
        return experience;
    }

    public Set<Rental> getRentals() {
        return Collections.unmodifiableSet(rentals);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (age: " + age + ", " + experience + ")";
    }
}
