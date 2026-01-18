package com.example.skipro.controller;

import com.example.skipro.dto.InstructorDto;
import com.example.skipro.model.Instructor;
import com.example.skipro.service.InstructorService;
import com.example.skipro.util.DtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Basic REST endpoints for Instructors.
 */
@RestController
@RequestMapping("/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> create(
            @RequestParam int yearsOfExperience,
            @RequestParam String birthDate,
            @RequestParam String lastName,
            @RequestParam String firstName,
            @RequestParam String qualificationLevel
    ) {
        Instructor instructor;
        try {
            instructor = new Instructor(
                    yearsOfExperience,
                    LocalDate.parse(birthDate),
                    lastName,
                    firstName,
                    qualificationLevel
            );
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

        instructorService.addInstructor(instructor);
        return ResponseEntity.ok(instructor.getId());
    }

    @GetMapping
    public List<InstructorDto> getAll() {
        return instructorService.getAllInstructors().stream()
                .map(DtoMapper::toInstructorDto)
                .toList();
    }
}
