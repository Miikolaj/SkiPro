package com.example.skipro.service;

import com.example.skipro.model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmploymentService {
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/employments.ser";
    private List<Employment> allEmployments = new ArrayList<>();

    public EmploymentService() {
        load();
    }

    public void addEmployment(Resort resort, Employee employee, LocalDate startDate) {
        Employment employment = new Employment(resort, employee, startDate);
        allEmployments.add(employment);
        save();
    }

    public List<Employment> getAllEmployments() {
        return allEmployments;
    }

    private void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            allEmployments = (List<Employment>) ois.readObject();
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
            oos.writeObject(allEmployments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}