package com.example.skipro.repository;

import com.example.skipro.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
}

