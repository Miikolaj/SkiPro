package com.example.skipro.service;

import com.example.skipro.model.Client;
import com.example.skipro.model.Equipment;
import com.example.skipro.model.Rental;
import com.example.skipro.model.enums.RentalStatus;
import com.example.skipro.repository.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service responsible for managing equipment rentals.
 */
@Service
public class RentalService {
    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    /**
     * Rents the specified equipment to the given client.
     * A new {@link Rental} is created, the equipment is marked as "in use",
     * and the rental is persisted to the database.
     *
     * @param client    the client renting the equipment
     * @param equipment the equipment to rent
     * @return the newly created rental
     * @throws IllegalStateException if the equipment is already in use
     */
    @Transactional
    public Rental rentEquipment(Client client, Equipment equipment) {
        if (equipment.isInUse()) {
            throw new IllegalStateException("Equipment is already rented out.");
        }

        Rental rental = new Rental(equipment, client);
        equipment.setInUse(true);
        return rentalRepository.save(rental);
    }

    /**
     * Processes the return of rented equipment and updates persistence.
     *
     * @param rental the rental to be returned
     */
    @Transactional
    public void returnEquipment(Rental rental) {
        rental.returnEquipment();
        rentalRepository.save(rental);
    }

    /**
     * Retrieves all active rentals for the specified client.
     *
     * @param client the client whose rentals should be queried
     * @return list of active rentals for the provided client
     */
    @Transactional(readOnly = true)
    public List<Rental> getActiveRentalsForClient(Client client) {
        if (client == null) return List.of();
        return rentalRepository.findByClientIdAndStatus(client.getId(), RentalStatus.ACTIVE);
    }
}
