package com.example.skipro.service;

import com.example.skipro.model.Client;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ClientService {
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/clients.ser";
    private List<Client> clients = new ArrayList<>();
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public ClientService() {
        load();
    }

    public void addClient(Client client) {
        clients.add(client);
        save();
    }

    public Client getClientById(UUID id) {
        return clients.stream()
                .filter(client -> client.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public String authenticate(String fullName, String password) {
        for (Client client : clients) {
            String clientFullName = client.getFirstName() + "." + client.getLastName();
            if (clientFullName.equalsIgnoreCase(fullName) && client.getPassword().equals(password)) {
                return generateToken(client);
            }
        }
        return null;
    }

    private String generateToken(Client client) {
        return Jwts.builder()
                .claim("id", client.getId())
                .claim("firstName", client.getFirstName())
                .claim("lastName", client.getLastName())
                .claim("age", client.getAge())
                .claim("experience", client.getExperience().toString())
                .setSubject(client.getFirstName() + "." + client.getLastName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SECRET_KEY)
                .compact();
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
