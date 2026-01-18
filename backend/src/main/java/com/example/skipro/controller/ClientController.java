package com.example.skipro.controller;

import com.example.skipro.model.Client;
import org.springframework.web.bind.annotation.*;
import com.example.skipro.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * REST controller exposing client‑authentication endpoints.
 */
@RestController
@RequestMapping("/auth")
public class ClientController {
    /**
     * Service used for authenticating clients and issuing JWT tokens.
     */
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Authenticates a client and returns a signed JWT token.
     * <p>
     * The client must supply their {@code fullName} in the format
     * {@code firstName.lastName} (case‑insensitive) and their plain‑text
     * {@code password}. On success a JWT token is returned; on failure the
     * response body is {@code null} (HTTP 200).
     *
     * @param fullName the client's full name in {@code firstName.lastName} format
     * @param password the client's password
     * @return a signed JWT token if authentication succeeds; otherwise {@code null}
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String fullName, @RequestParam String password) {
        String token = clientService.authenticate(fullName, password);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(token);
    }

    /**
     * Registers a new client in the system.
     * <p>
     * The client must provide their details in the request body, including
     * their {@code firstName}, {@code lastName}, {@code email}, and {@code password}.
     * On success, a confirmation message or token is returned; on failure, an
     * appropriate error response is generated.
     *
     * @param client the client object containing registration details
     * @return a confirmation message or token if registration succeeds
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Client client) {
        String token = clientService.register(client);
        if (token == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }
}
