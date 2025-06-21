package com.example.skipro.service;

import com.example.skipro.model.Employee;
import com.example.skipro.model.Employment;
import com.example.skipro.model.Resort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service responsible for managing ski resorts and employment relations.
 */
public class ResortService {
    private final List<Resort> resorts = new ArrayList<>();
    private final List<Employment> employments = new ArrayList<>();

    /**
     * Creates a new resort.
     */
    public Resort createResort(String name, String location) {
        Resort resort = new Resort(name, location);
        resorts.add(resort);
        return resort;
    }

    /**
     * Employs a person in the specified resort.
     */
    public Employment employPerson(Resort resort, Employee employee, LocalDate startDate) {
        Employment employment = new Employment(resort, employee, startDate);
        employments.add(employment);
        return employment;
    }

    /**
     * Returns all resorts.
     */
    public List<Resort> getAllResorts() {
        return Collections.unmodifiableList(resorts);
    }

    /**
     * Finds resort by name.
     */
    public Optional<Resort> findResortByName(String name) {
        return resorts.stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Returns all employments in the system.
     */
    public List<Employment> getAllEmployments() {
        return Collections.unmodifiableList(employments);
    }

    /**
     * Returns employments for a given employee.
     */
    public List<Employment> getEmploymentsForEmployee(Employee employee) {
        return employments.stream()
                .filter(e -> e.getEmployee().equals(employee))
                .toList();
    }

    /**
     * Returns employees employed at a given resort.
     */
    public List<Employee> getEmployeesInResort(Resort resort) {
        return employments.stream()
                .filter(e -> e.getResort().equals(resort))
                .map(Employment::getEmployee)
                .toList();
    }
}
