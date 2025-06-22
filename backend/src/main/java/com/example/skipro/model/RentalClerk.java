package com.example.skipro.model;


import java.io.Serializable;
import java.time.LocalDate;

public class RentalClerk extends Employee implements Serializable {
    private int rentalsHandled = 0;

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
