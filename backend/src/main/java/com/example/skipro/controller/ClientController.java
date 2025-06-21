package com.example.skipro.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.skipro.service.ClientService;

@RestController
@RequestMapping("/auth")
public class ClientController {
    private final ClientService clientService = new ClientService();

    @PostMapping("/login")
    public String login(@RequestParam String fullName, @RequestParam String password) {
        return clientService.authenticate(fullName, password);
    }
}
