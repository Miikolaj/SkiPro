package com.example.skipro.repository;

import com.example.skipro.model.Resort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResortRepository extends JpaRepository<Resort, UUID> {
}

