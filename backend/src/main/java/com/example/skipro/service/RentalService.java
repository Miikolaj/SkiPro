package com.example.skipro.service;

import com.example.skipro.model.Client;
import com.example.skipro.model.Equipment;
import com.example.skipro.model.Rental;
import com.example.skipro.model.RentalClerk;
import com.example.skipro.model.enums.RentalStatus;
import com.example.skipro.repository.EquipmentRepository;
import com.example.skipro.repository.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service responsible for managing equipment rentals.
 */
@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final EquipmentRepository equipmentRepository;
    private final RentalClerkService rentalClerkService;

    public RentalService(
            RentalRepository rentalRepository,
            EquipmentRepository equipmentRepository,
            RentalClerkService rentalClerkService
    ) {
        this.rentalRepository = rentalRepository;
        this.equipmentRepository = equipmentRepository;
        this.rentalClerkService = rentalClerkService;
    }

    /**
     * Rents the specified equipment to the given client and records which clerk processed it.
     * PDF-compliant: caller provides plannedReturnDate ("planowana data zwrotu").
     */
    @Transactional
    public Rental rentEquipment(Client client, Equipment equipment, java.time.LocalDateTime plannedReturnDate, UUID rentalClerkId) {
        if (client == null || equipment == null) {
            throw new IllegalArgumentException("Client and equipment must not be null");
        }
        if (plannedReturnDate == null) {
            throw new IllegalArgumentException("plannedReturnDate must not be null");
        }
        if (rentalClerkId == null) {
            throw new IllegalArgumentException("rentalClerkId must not be null");
        }
        if (equipment.isInUse()) {
            throw new IllegalStateException("Equipment is already rented out.");
        }

        RentalClerk clerk = rentalClerkService.getRentalClerkById(rentalClerkId);
        if (clerk == null) {
            throw new IllegalArgumentException("RentalClerk not found: " + rentalClerkId);
        }

        Rental rental = new Rental(equipment, client, clerk);
        rental.setPlannedReturnDate(plannedReturnDate);

        equipment.setInUse(true);
        equipmentRepository.save(equipment);

        // business action: clerk handled one more rental
        rentalClerkService.incrementRentalsHandled(clerk.getId());

        return rentalRepository.save(rental);
    }

    /**
     * Creates a rental without a clerk assigned (kept for demo/backward compatibility).
     */
    @Transactional
    public Rental rentEquipment(Client client, Equipment equipment) {
        if (client == null || equipment == null) {
            throw new IllegalArgumentException("Client and equipment must not be null");
        }
        if (equipment.isInUse()) {
            throw new IllegalStateException("Equipment is already rented out.");
        }

        Rental rental = new Rental(equipment, client, null);
        equipment.setInUse(true);
        equipmentRepository.save(equipment);
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
        equipmentRepository.save(rental.getEquipment());
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

    @Transactional(readOnly = true)
    public Rental getRentalById(UUID id) {
        return id == null ? null : rentalRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }
}
