package com.example.skipro.service;

import com.example.skipro.model.RentalClerk;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalClerkService {
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/rentalclerks.ser";
    private List<RentalClerk> allRentalClerks = new ArrayList<>();

    public RentalClerkService() {
        load();
    }

    public void addRentalClerk(RentalClerk clerk) {
        allRentalClerks.add(clerk);
        save();
    }

    public List<RentalClerk> getAllRentalClerks() {
        return allRentalClerks;
    }

    private void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            allRentalClerks = (List<RentalClerk>) ois.readObject();
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
            oos.writeObject(allRentalClerks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}