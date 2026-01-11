package com.example.skipro.service;

import com.example.skipro.model.Employment;
import com.example.skipro.repository.EmploymentRepository;
import com.example.skipro.repository.InstructorRepository;
import com.example.skipro.repository.ResortRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class EmploymentService {
    private final EmploymentRepository employmentRepository;
    private final ResortRepository resortRepository;
    private final InstructorRepository instructorRepository;

    public EmploymentService(
            EmploymentRepository employmentRepository,
            ResortRepository resortRepository,
            InstructorRepository instructorRepository
    ) {
        this.employmentRepository = employmentRepository;
        this.resortRepository = resortRepository;
        this.instructorRepository = instructorRepository;
    }

    @Transactional
    public Employment addEmployment(UUID resortId, UUID instructorId, LocalDate startDate) {
        var resort = resortRepository.findById(resortId)
                .orElseThrow(() -> new IllegalArgumentException("Resort not found: " + resortId));
        var instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found: " + instructorId));

        Employment employment = new Employment(resort, instructor, startDate);
        return employmentRepository.save(employment);
    }

    @Transactional(readOnly = true)
    public List<Employment> getAllEmployments() {
        return employmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Employment> getEmploymentsForResort(UUID resortId) {
        return employmentRepository.findByResortId(resortId);
    }

    @Transactional(readOnly = true)
    public List<Employment> getEmploymentsForInstructor(UUID instructorId) {
        return employmentRepository.findByInstructorId(instructorId);
    }
}