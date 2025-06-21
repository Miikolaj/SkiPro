package com.example.skipro.model;

import com.example.skipro.model.enums.Experience;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a ski resort client (customer) who can participate in lessons.
 */
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final String firstName;
    private final String lastName;
    private final int age;
    private final Experience experience;
    private String password; // Assuming password is needed for authentication

    /**
     * Association: 0..* Lessons the client is enrolled in
     */
    private final Set<Lesson> lessons = new HashSet<>();
    private final Set<Rental> rentals = new HashSet<>();

    public Client(String firstName, String lastName, int age, Experience experience, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.experience = experience;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public String setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = password;
        return this.password;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (age: " + age + ", " + experience + ")";
    }
}
