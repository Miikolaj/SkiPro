package com.example.skipro.service;

import com.example.skipro.model.*;
import com.example.skipro.util.PersistenceManager;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing employments and persisting them to a file.
 */
@Service
public class EmploymentService {
    private final PersistenceManager<Employment> persistence = new PersistenceManager<>("src/main/java/com/example/skipro/data/employments.ser"); // Name of the file used for saving employments.
    private List<Employment> allEmployments = new ArrayList<>(); // List containing all employments.

    /**
     * Constructs an EmploymentService and loads employments from file.
     */
    public EmploymentService() {
        allEmployments = persistence.load();
    }

    /**
     * Adds a new employment entry and persists the change.
     *
     * @param resort    the resort where the employee works
     * @param employee  the employee being employed
     * @param startDate the start date of the employment
     */
    public void addEmployment(Resort resort, Employee employee, LocalDate startDate) {
        Employment employment = new Employment(resort, employee, startDate);
        allEmployments.add(employment);
        persistence.save(allEmployments);
    }
}