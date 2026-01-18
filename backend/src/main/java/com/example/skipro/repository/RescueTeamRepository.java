package com.example.skipro.repository;

import com.example.skipro.model.RescueTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RescueTeamRepository extends JpaRepository<RescueTeam, UUID> {
}

