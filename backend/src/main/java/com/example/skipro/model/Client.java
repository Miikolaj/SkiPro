package com.example.skipro.model;

import com.example.skipro.model.enums.Experience;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a ski‑resort client (customer) who can participate in lessons, rent equipment,
 * and authenticate within the system.
 * <p>
 * Each client is uniquely identified by an immutable {@link UUID} generated at construction time.
 * The personal data—first name, last name, and age—together with the client’s skiing
 * {@link Experience} level are used by various services (pricing, eligibility checks, etc.).
 * </p>
 * <p>
 * The class also keeps two bidirectional associations:
 * <ul>
 *   <li>{@code lessons} — all {@link Lesson} instances the client is enrolled in.</li>
 *   <li>{@code rentals} — all {@link Rental} transactions made by the client.</li>
 * </ul>
 * Only basic state is stored here; business logic is delegated to service classes.
 * </p>
 */
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID(); /** Unique, immutable identifier for the client. */
    private final String firstName;  /** Client’s first name. */
    private final String lastName;  /** Client’s last name. */
    private final int age;  /** Client’s age in years. */
    private final Experience experience; /** Self‑declared skiing experience level. */
    private String password;   /** Plain‑text password used for authentication */

    /** Association: 0..* lessons the client is enrolled in. */
    private final Set<Lesson> lessons = new HashSet<>();
    /** Association: 0..* equipment rentals linked to this client. */
    private final Set<Rental> rentals = new HashSet<>();

    /**
     * Constructs a new {@code Client} with the given personal details and experience level.
     *
     * @param firstName  client’s first name
     * @param lastName   client’s last name
     * @param age        client’s age in years
     * @param experience client’s skiing experience level
     * @param password   plain‑text password for authentication
     */
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
