package com.example.skipro.service;

import com.example.skipro.model.RentalClerk;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing rental clerks and persisting them to a file.
 */
@Service
public class RentalClerkService {
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/rentalclerks.ser"; // Name of the file used for saving rental clerks.
    private List<RentalClerk> allRentalClerks = new ArrayList<>(); // List containing all rental clerks.

    /**
     * Constructs a RentalClerkService and loads clerks from file.
     */
    public RentalClerkService() {
        load();
    }

    /**
     * Adds a rental clerk to the system and persists the change.
     *
     * @param clerk the clerk to add
     */
    public void addRentalClerk(RentalClerk clerk) {
        allRentalClerks.add(clerk);
        save();
    }

    /**
     * Returns a mutable list of all rental clerks.
     *
     * @return list of rental clerks
     */
    public List<RentalClerk> getAllRentalClerks() {
        return allRentalClerks;
    }

    /**
     * Loads rental clerks from the persistent file into memory. If the file does not exist,
     * the method returns silently.
     */
    private void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            allRentalClerks = (List<RentalClerk>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current list of rental clerks to a file. Necessary directories are
     * created automatically.
     */
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