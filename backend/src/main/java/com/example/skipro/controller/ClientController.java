package com.example.skipro.controller;

import com.example.skipro.model.Client;
import com.example.skipro.model.Instructor;
import com.example.skipro.service.InstructorService;
import org.springframework.web.bind.annotation.*;
import com.example.skipro.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class ClientController {
    private final ClientService clientService = new ClientService();

    @PostMapping("/login")
    public String login(@RequestParam String fullName, @RequestParam String password) {
        return clientService.authenticate(fullName, password);
    }
}
