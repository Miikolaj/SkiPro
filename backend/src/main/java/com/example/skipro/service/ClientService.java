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

/**
 * Service responsible for managing clients, authentication, and token generation.
 */
public class ClientService {
    private static final String FILE_NAME = "src/main/java/com/example/skipro/data/clients.ser"; // Name of the file used for saving clients.
    private List<Client> clients = new ArrayList<>(); // List containing all registered clients.
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Secret key used for signing JWT tokens.

    /**
     * Constructs a ClientService and loads clients from file.
     */
    public ClientService() {
        load();
    }

    /**
     * Adds a client to the registry and persists the change.
     *
     * @param client the client to add
     */
    public void addClient(Client client) {
        clients.add(client);
        save();
    }

    /**
     * Registers a new client, persists the change, and returns a signed JWT token.
     *
     * @param client the client to register
     * @return signed JWT token string
     */
    public String register(Client client) {
        addClient(client);
        return generateToken(client);
    }

    /**
     * Retrieves a client by identifier.
     *
     * @param id the client identifier
     * @return the client if found; otherwise {@code null}
     */
    public Client getClientById(UUID id) {
        return clients.stream()
                .filter(client -> client.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Authenticates a client and, if successful, returns a signed JWT token.
     * <p>
     * Authentication matches against the pattern {@code firstName.lastName} (case‑insensitive)
     * and the plain‑text password stored for the client.
     *
     * @param fullName the client name in the format {@code firstName.lastName}
     * @param password the client password
     * @return a JWT token if authentication succeeds; otherwise {@code null}
     */
    public String authenticate(String fullName, String password) {
        for (Client client : clients) {
            String clientFullName = client.getFirstName() + "." + client.getLastName();
            if (clientFullName.equalsIgnoreCase(fullName) && client.getPassword().equals(password)) {
                return generateToken(client);
            }
        }
        return null;
    }

    /**
     * Generates a JWT token for the given client.
     *
     * @param client the client for whom the token is generated
     * @return signed JWT token string
     */
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

    /**
     * Loads clients from the persistent file into memory. If the file does not exist, the method returns silently.
     */
    private void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            clients = (List<Client>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current list of clients to a file. Necessary directories are created automatically.
     */
    private void save() {
        File file = new File(FILE_NAME);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(clients);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
