package com.example.skipro.service;

import com.example.skipro.model.Client;
import com.example.skipro.model.Equipment;
import com.example.skipro.model.Rental;
import com.example.skipro.model.enums.RentalStatus;

import java.util.ArrayList;
import java.util.List;

public class RentalService {
    private final List<Rental> allRentals = new ArrayList<>();

    public Rental rentEquipment(Client client, Equipment equipment) {
        if(equipment.isInUse()) {
            throw new IllegalStateException("Equipment is already rented out.");
        }

        Rental rental = new Rental(equipment, client);
        equipment.setInUse(true);
        client.addRental(rental);
        allRentals.add(rental);
        return rental;
    }

    public void returnEquipment(Rental rental) {
        rental.returnEquipment();
    }

    public List<Rental> getActiveRentalsForClient(Client client) {
        return allRentals.stream()
                .filter(r -> r.getClient().equals(client))
                .filter(r -> r.getStatus() == RentalStatus.ACTIVE)
                .toList();
    }
}
