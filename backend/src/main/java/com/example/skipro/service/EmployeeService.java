package com.example.skipro.service;

import com.example.skipro.model.Employee;
import com.example.skipro.model.Instructor;
import com.example.skipro.model.RentalClerk;
import com.example.skipro.model.RescueWorker;

import java.util.*;

/**
 * Service responsible for managing employees of all types.
 */
public class EmployeeService {
    private final List<Employee> employees = new ArrayList<>();

    public void registerEmployee(Employee employee) {
        employees.add(employee);
    }

    public List<Employee> getAllEmployees() {
        return Collections.unmodifiableList(employees);
    }

    public List<Instructor> getAllInstructors() {
        return employees.stream()
                .filter(e -> e instanceof Instructor)
                .map(e -> (Instructor) e)
                .toList();
    }

    public List<RescueWorker> getAllRescueWorkers() {
        return employees.stream()
                .filter(e -> e instanceof RescueWorker)
                .map(e -> (RescueWorker) e)
                .toList();
    }

    public List<RentalClerk> getAllRentalClerks() {
        return employees.stream()
                .filter(e -> e instanceof RentalClerk)
                .map(e -> (RentalClerk) e)
                .toList();
    }

    public Optional<Employee> findByFullName(String firstName, String lastName) {
        return employees.stream()
                .filter(e -> e.getFirstName().equalsIgnoreCase(firstName))
                .filter(e -> e.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }
}
