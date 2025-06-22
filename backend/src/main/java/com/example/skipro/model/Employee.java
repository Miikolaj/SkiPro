package com.example.skipro.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class Employee implements Serializable {
    protected String firstName;
    protected String lastName;
    protected int age;
    protected LocalDate birthDate;
    protected int yearsOfExperience; // in years

    private final Set<Employment> employments = new HashSet<>();

    public Employee() {
    }

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
