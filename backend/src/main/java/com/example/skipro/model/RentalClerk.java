package com.example.skipro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a rental-clerk employee responsible for handling equipment rentals at the resort.
 * <p>
 * A {@code RentalClerk} extends {@link Employee} and tracks the number of rentals they have processed.
 * Salary is computed based on a base wage plus a fee per rental handled.
 * </p>
 */
@Entity
@Table(name = "rental_clerks")
public class RentalClerk extends Employee {
    private int rentalsHandled = 0;

    /**
     * Rentals processed by this clerk (inverse side of Rental -> RentalClerk).
     * Not used in current GUI flow, but useful for future screens.
     */
    @OneToMany(mappedBy = "rentalClerk")
    private Set<Rental> rentals = new HashSet<>();

    protected RentalClerk() {
        // for JPA
    }

    /**
     * Constructs a {@code RentalClerk} with the provided personal information and initial rentals count.
     *
     * @param yearsOfExperience total years of work experience
     * @param birthDate         date of birth
     * @param lastName          clerk's last name
     * @param firstName         clerk's first name
     * @param rentalsHandled    initial number of rentals processed
     */
    public RentalClerk(int yearsOfExperience, LocalDate birthDate, String lastName, String firstName, int rentalsHandled) {
        super(yearsOfExperience, birthDate, lastName, firstName);
        this.rentalsHandled = rentalsHandled;
    }

    public int getRentalsHandled() {
        return rentalsHandled;
    }

    public Set<Rental> getRentals() {
        return Collections.unmodifiableSet(rentals);
    }

    public void incrementRentalsHandled() {
        rentalsHandled++;
    }

    @Override
    public String getRole() {
        return "Rental Clerk";
    }

    @Override
    public double calculateSalary() {
        return 2800.0 + rentalsHandled * 10.0;
    }
}
