package com.example.skipro.model;

import com.example.skipro.model.enums.Experience;
import jakarta.persistence.*;

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
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;
    private String lastName;
    private int age;

    @Enumerated(EnumType.STRING)
    private Experience experience;

    /** Stored as BCrypt hash (never store plaintext in production). */
    @Column(name = "password_hash")
    private String password;

    @ManyToMany(mappedBy = "clients")
    private Set<Lesson> lessons = new HashSet<>();

    protected Client() {
        // for JPA
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = password;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (age: " + age + ", " + experience + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
