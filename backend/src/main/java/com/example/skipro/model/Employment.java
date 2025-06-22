package com.example.skipro.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents the employment relationship between an {@link Employee} and a {@link Resort}.
 * <p>
 * Each {@code Employment} record captures the start and (optionally) end dates of the contract.
 * The association is <strong>bidirectional</strong>: upon construction the new instance automatically
 * adds itself to both {@code Resort} and {@code Employee} via their respective helper methods.
 * </p>
 */
public class Employment implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id = UUID.randomUUID(); /** Unique, immutable identifier for the employment contract. */
    private final Resort resort; /** Resort where the employee works. */
    private final Employee employee; /** Employee involved in this contract. */
    private final LocalDate startDate; /** Contract start date (inclusive). */
    private LocalDate endDate; /** Contract end date (inclusive); {@code null} while the employment is ongoing. */
    private Employment employment; /** Self-reference placeholder  */

    /**
     * Creates a new employment contract linking the given employee to the specified resort.
     * The new contract is added to both sides of the association.
     *
     * @param resort    the resort where the employee will work
     * @param employee  the employee being hired
     * @param startDate the start date of the employment
     */
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
