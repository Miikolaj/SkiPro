package com.example.skipro.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a rental-clerk employee responsible for handling equipment rentals at the resort.
 * <p>
 * A {@code RentalClerk} extends {@link Employee} and tracks the number of rentals they have processed.
 * Salary is computed based on a base wage plus a fee per rental handled.
 * </p>
 */
public class RentalClerk extends Employee implements Serializable {
    private int rentalsHandled = 0;

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
