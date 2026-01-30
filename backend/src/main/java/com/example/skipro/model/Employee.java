package com.example.skipro.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Abstract base class for every staff member employed by the ski resort (e.g. instructors, rescue workers).
 * <p>
 * The class captures common personal data such as first/last name, birth date, and accumulated professional
 * experience. It also maintains an <em>association</em> to the set of {@link Employment} records that link an
 * {@code Employee} to one or more {@link Resort}s over time.
 * </p>
 * <p>
 * Concrete subclasses must implement {@link #getRole()} and {@link #calculateSalary()} to provide role‑specific
 * information and salary computation logic.
 * </p>
 */
@Entity
@Table(name = "employees")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employee {
    // Primary key shared by all Employee subclasses (e.g., Instructor)
    @Id
    @GeneratedValue
    protected UUID id;

    protected String firstName;
    /**
     * Employee first name.
     */
    protected String lastName;
    /**
     * Employee last name.
     */
    protected int age;
    /**
     * Employee age in years (derived from {@link #birthDate}).
     */
    protected LocalDate birthDate;
    /**
     * Date of birth.
     */
    protected int yearsOfExperience;
    /**
     * Total years of professional experience.
     */

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<Employment> employments = new HashSet<>();

    public Employee() {
    }

    /**
     * Constructs an {@code Employee} with the provided personal details and experience.
     *
     * @param yearsOfExperience years of professional experience
     * @param birthDate         date of birth
     * @param lastName          employee last name
     * @param firstName         employee first name
     */
    public Employee(int yearsOfExperience, LocalDate birthDate, String lastName, String firstName) {
        this.yearsOfExperience = yearsOfExperience;
        this.birthDate = birthDate;
        this.age = calculateAge();
        this.lastName = lastName;
        this.firstName = firstName;
    }

    private int calculateAge() {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return getFullName() + " (" + getClass().getSimpleName() + ")";
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<Employment> getEmployments() {
        return java.util.Collections.unmodifiableSet(employments);
    }

    void addEmployment(Employment employment) {
        if (employment == null) return;
        employments.add(employment);
        employment.setEmployee(this);
    }

    void removeEmployment(Employment employment) {
        if (employment == null) return;
        employments.remove(employment);
    }

    public abstract String getRole();

    public abstract double calculateSalary();
}
