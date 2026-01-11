package com.example.skipro.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
@MappedSuperclass
public abstract class Employee implements Serializable {
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

    @Transient
    private Set<Employment> employments = new HashSet<>();

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
        Date currentDate = new Date();
        return currentDate.getYear() - birthDate.getYear();
    }

    public void addEmployment(Employment e) {
        employments.add(e);
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

    public int getAge() {
        return age;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public Set<Employment> getEmployments() {
        return Collections.unmodifiableSet(employments);
    }

    public abstract String getRole();

    public abstract double calculateSalary();
}
