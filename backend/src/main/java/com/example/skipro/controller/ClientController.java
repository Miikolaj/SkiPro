package com.example.skipro.controller;

import org.springframework.web.bind.annotation.*;
import com.example.skipro.service.ClientService;

/**
 * REST controller exposing client‑authentication endpoints.
 */
@RestController
@RequestMapping("/auth")
public class ClientController {
    /**
     * Service used for authenticating clients and issuing JWT tokens.
     */
    private final ClientService clientService = new ClientService();

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
    public String login(@RequestParam String fullName, @RequestParam String password) {
        return clientService.authenticate(fullName, password);
    }
}
