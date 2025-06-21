package com.example.skipro.model;

import java.time.LocalDate;
import java.util.UUID;

public class Employment {
    private final UUID id = UUID.randomUUID();
    private final Resort resort;
    private final Employee employee;
    private final LocalDate startDate;
    private LocalDate endDate;

    public Employment(Resort resort, Employee employee, LocalDate startDate) {
        this.resort = resort;
        this.employee = employee;
        this.startDate = startDate;

        resort.addEmployment(this);
        employee.addEmployment(this);
    }

    public UUID getId() {
        return id;
    }

    public Resort getResort() {
        return resort;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void endEmployment(LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return employee + " employed at " + resort + " from " + startDate + (endDate != null ? " to " + endDate : "");
    }
}
