package com.example.skipro.controller;

import com.example.skipro.dto.EmployeeDto;
import com.example.skipro.service.EmployeeService;
import com.example.skipro.util.DtoMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Read-only endpoints for Employees.
 * <p>
 * Employees are stored using JPA inheritance (JOINED), so this returns a heterogeneous list
 * (e.g., Instructor, RescueWorker, RentalClerk).
 * </p>
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDto> getAll() {
        return employeeService.getAllEmployees().stream()
                .map(DtoMapper::toEmployeeDto)
                .toList();
    }
}
