package com.example.skipro.service;

import com.example.skipro.model.Client;
import com.example.skipro.model.Equipment;
import com.example.skipro.model.Rental;
import com.example.skipro.model.enums.Experience;
import com.example.skipro.model.enums.RentalStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RentalService {
    private List<Rental> allRentals = new ArrayList<>();
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/rentals.ser";

    public RentalService() {
        load();
    }

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

    public void returnEquipment(Rental rental) {
        rental.returnEquipment();
    }

    public List<Rental> getActiveRentalsForClient(Client client) {
        return allRentals.stream()
                .filter(r -> r.getClient().equals(client))
                .filter(r -> r.getStatus() == RentalStatus.ACTIVE)
                .toList();
    }

    private void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            allRentals = (List<Rental>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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
