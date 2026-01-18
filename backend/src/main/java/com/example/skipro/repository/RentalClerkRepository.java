package com.example.skipro.repository;

import com.example.skipro.model.RentalClerk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RentalClerkRepository extends JpaRepository<RentalClerk, UUID> {
}

