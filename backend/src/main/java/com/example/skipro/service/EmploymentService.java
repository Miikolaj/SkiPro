package com.example.skipro.service;

import com.example.skipro.model.Employment;
import com.example.skipro.repository.EmploymentRepository;
import com.example.skipro.repository.EmployeeRepository;
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
    private final EmployeeRepository employeeRepository;

    public EmploymentService(
            EmploymentRepository employmentRepository,
            ResortRepository resortRepository,
            EmployeeRepository employeeRepository
    ) {
        this.employmentRepository = employmentRepository;
        this.resortRepository = resortRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Employment addEmployment(UUID resortId, UUID employeeId, LocalDate startDate) {
        var resort = resortRepository.findById(resortId)
                .orElseThrow(() -> new IllegalArgumentException("Resort not found: " + resortId));
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));

        Employment employment = new Employment(resort, employee, startDate);
        return employmentRepository.save(employment);
    }
}