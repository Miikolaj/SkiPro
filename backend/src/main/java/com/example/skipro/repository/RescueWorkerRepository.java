package com.example.skipro.repository;

import com.example.skipro.model.RescueWorker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RescueWorkerRepository extends JpaRepository<RescueWorker, UUID> {

}

