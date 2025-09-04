package com.example.skipro.service;

import com.example.skipro.model.Client;
import com.example.skipro.model.Equipment;
import com.example.skipro.model.Rental;
import com.example.skipro.model.enums.Experience;
import com.example.skipro.model.enums.RentalStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing equipment rentals and persisting them to a file.
 */
@Service
public class RentalService {
    private List<Rental> allRentals = new ArrayList<>(); //List containing all rentals.
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/rentals.ser"; //Name of the file used for saving rentals.

    /**
     * Constructs a RentalService and loads rentals from file.
     */
    public RentalService() {
        load();
    }

    /**
     * Rents the specified equipment to the given client.
     * A new {@link Rental} is created, the equipment is marked as "in use",
     * and the rental is persisted to the internal list and file.
     *
     * @param client    the client renting the equipment
     * @param equipment the equipment to rent
     * @return the newly created rental
     * @throws IllegalStateException if the equipment is already in use
     */
    public Rental rentEquipment(Client client, Equipment equipment) {
        if(equipment.isInUse()) {
            throw new IllegalStateException("Equipment is already rented out.");
        }

        Rental rental = new Rental(equipment, client);
        equipment.setInUse(true);
        client.addRental(rental);
        allRentals.add(rental);
        save();
        return rental;
    }

    /**
     * Processes the return of rented equipment and updates persistence.
     *
     * @param rental the rental to be returned
     */
    public void returnEquipment(Rental rental) {
        rental.returnEquipment();
        save();
    }

    /**
     * Retrieves all active rentals for the specified client.
     *
     * @param client the client whose rentals should be queried
     * @return list of active rentals for the provided client
     */
    public List<Rental> getActiveRentalsForClient(Client client) {
        return allRentals.stream()
                .filter(r -> r.getClient().equals(client))
                .filter(r -> r.getStatus() == RentalStatus.ACTIVE)
                .toList();
    }

    /**
     * Loads rentals from the persistent file into memory. If the file does not
     * exist, the method returns silently.
     */
    private void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            allRentals = (List<Rental>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current list of rentals to a file. Necessary directories are
     * created automatically.
     */
    private void save() {
        File file = new File(FILE_NAME);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(allRentals);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
