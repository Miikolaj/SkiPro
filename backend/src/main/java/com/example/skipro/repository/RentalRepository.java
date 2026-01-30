package com.example.skipro.repository;

import com.example.skipro.model.Rental;
import com.example.skipro.model.enums.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RentalRepository extends JpaRepository<Rental, UUID> {
}

