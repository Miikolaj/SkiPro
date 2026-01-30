package com.example.skipro.service;

import com.example.skipro.config.AppJwtProperties;
import com.example.skipro.model.Client;
import com.example.skipro.repository.ClientRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

/**
 * Service responsible for managing clients, authentication, and token generation.
 */
@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final SecretKey secretKey;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ClientService(ClientRepository clientRepository, AppJwtProperties jwtProperties) {
        this.clientRepository = clientRepository;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    public void addClient(Client client) {
        clientRepository.save(client);
    }

    public String register(Client client) {
        if (client == null || client.getPassword() == null) return null;

        // store hashed password
        client.setPassword(passwordEncoder.encode(client.getPassword()));

        addClient(client);
        return generateToken(client);
    }

    public Client getClientById(UUID id) {
        return id == null ? null : clientRepository.findById(id).orElse(null);
    }

    /**
     * Authenticates a client and, if successful, returns a signed JWT token.
     * Authentication matches against the pattern {@code firstName.lastName} (case‑insensitive)
     * and the plain‑text password stored for the client.
     */
    public String authenticate(String fullName, String password) {
        if (fullName == null || password == null) return null;

        String[] parts = fullName.split("\\.", 2);
        if (parts.length != 2) return null;

        String firstName = parts[0].trim();
        String lastName = parts[1].trim();
        if (firstName.isEmpty() || lastName.isEmpty()) return null;

        Client client = clientRepository.findAll().stream()
                .filter(c -> c.getFirstName() != null && c.getLastName() != null)
                .filter(c -> c.getFirstName().equalsIgnoreCase(firstName)
                        && c.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);

        if (client == null) return null;

        String stored = client.getPassword();
        if (stored == null) return null;

        // verify hashed password (BCrypt)
        boolean ok = passwordEncoder.matches(password, stored);
        if (!ok) return null;

        return generateToken(client);
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
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(secretKey)
                .compact();
    }
}
