package com.example.skipro.service;

import com.example.skipro.model.Client;
import com.example.skipro.model.enums.Experience;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/clients.ser";
    private List<Client> clients = new ArrayList<>();

    public ClientService() {
        load();
    }

    public void addClient(Client client) {
        clients.add(client);
        save();
    }

    public boolean authenticate(String fullName, String password) {
        for (Client client : clients) {
            String clientFullName = client.getFirstName() + "." + client.getLastName();
            if (clientFullName.equalsIgnoreCase(fullName) && client.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            clients = (List<Client>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        File file = new File(FILE_NAME);
        File dir = file.getParentFile();
        if(dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(clients);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
