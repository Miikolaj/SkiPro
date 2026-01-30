package com.example.skipro.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents the employment relationship between an {@link Employee} and a {@link Resort}.
 * <p>
 * Association-class ("atrybut asocjacji") for the 1-* relations:
 * Resort (1) -> Employment (*) and Employee (1) -> Employment (*).
 * </p>
 */
@Entity
@Table(name = "employments")
public class Employment {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private Resort resort;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate startDate;

    /**
     * Nullable = still employed (termination date not yet set).
     */
    @Column(nullable = true)
    private LocalDate endDate;

    protected Employment() {
        // for JPA
    }

    public Employment(Resort resort, Employee employee, LocalDate startDate) {
        if (resort == null || employee == null || startDate == null) {
            throw new IllegalArgumentException("Resort, employee and startDate must not be null");
        }
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
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        this.endDate = endDate;
    }

    void setResort(Resort resort) {
        this.resort = resort;
    }

    void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return (employee != null ? employee.getFullName() : "<employee>") +
                " employed at " + (resort != null ? resort.getName() : "<resort>") +
                " from " + startDate + (endDate != null ? " to " + endDate : "");
    }
}
