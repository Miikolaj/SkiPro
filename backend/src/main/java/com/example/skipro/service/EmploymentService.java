package com.example.skipro.service;

import com.example.skipro.model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing employments and persisting them to a file.
 */
public class EmploymentService {
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/employments.ser"; // Name of the file used for saving employments.
    private List<Employment> allEmployments = new ArrayList<>(); // List containing all employments.

    /**
     * Constructs an EmploymentService and loads employments from file.
     */
    public EmploymentService() {
        load();
    }

    /**
     * Adds a new employment entry and persists the change.
     *
     * @param resort    the resort where the employee works
     * @param employee  the employee being employed
     * @param startDate the start date of the employment
     */
    public void addEmployment(Resort resort, Employee employee, LocalDate startDate) {
        Employment employment = new Employment(resort, employee, startDate);
        allEmployments.add(employment);
        save();
    }

    /**
     * Returns a mutable list of all employments.
     *
     * @return list of employments
     */
    public List<Employment> getAllEmployments() {
        return allEmployments;
    }

    /**
     * Loads employments from the persistent file into memory. If the file does not exist,
     * the method returns silently.
     */
    private void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            allEmployments = (List<Employment>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current list of employments to a file. Necessary directories are created automatically.
     */
    private void save() {
        File file = new File(FILE_NAME);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(allEmployments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}