package com.example.skipro.repository;

import com.example.skipro.model.Employment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmploymentRepository extends JpaRepository<Employment, UUID> {
    List<Employment> findByResortId(UUID resortId);

    List<Employment> findByInstructorId(UUID instructorId);
}

