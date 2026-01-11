package com.example.skipro.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents the employment relationship between an {@link Instructor} and a {@link Resort}.
 * <p>
 * This is an <strong>association-class</strong> ("atrybut asocjacji") for the 1-* relations:
 * Resort (1) -> Employment (*) and Instructor (1) -> Employment (*).
 * It stores attributes that belong to the connection itself, e.g. start/end dates.
 * </p>
 */
@Entity
@Table(name = "employments")
public class Employment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    /** Resort where the employee works. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private Resort resort;

    /** Employee involved in this contract (persisted subtype: Instructor). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    /** Contract start date (inclusive). */
    @Column(nullable = false)
    private LocalDate startDate;

    /** Contract end date (inclusive); {@code null} while the employment is ongoing. */
    private LocalDate endDate;

    protected Employment() {
        // for JPA
    }

    /**
     * Creates a new employment contract linking the given instructor to the specified resort.
     * The new contract is added to both sides of the association.
     */
    public Employment(Resort resort, Instructor instructor, LocalDate startDate) {
        if (resort == null || instructor == null || startDate == null) {
            throw new IllegalArgumentException("Resort, instructor and startDate must not be null");
        }
        this.startDate = startDate;
        // set both sides via helpers to keep bidirectional consistency
        resort.addEmployment(this);
        instructor.addEmployment(this);
    }

    UUID getIdInternal() {
        return id;
    }

    public UUID getId() {
        return id;
    }

    public Resort getResort() {
        return resort;
    }

    public Instructor getInstructor() {
        return instructor;
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

    // Package-private setters used by helper methods to keep both sides consistent
    void setResort(Resort resort) {
        this.resort = resort;
    }

    void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return (instructor != null ? instructor.getFullName() : "<instructor>") +
                " employed at " + (resort != null ? resort.getName() : "<resort>") +
                " from " + startDate + (endDate != null ? " to " + endDate : "");
    }
}
