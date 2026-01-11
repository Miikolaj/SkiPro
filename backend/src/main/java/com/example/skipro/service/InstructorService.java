package com.example.skipro.service;

import com.example.skipro.model.Instructor;
import com.example.skipro.repository.InstructorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service responsible for managing instructors.
 */
@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    /**
     * Adds a new instructor to the registry and persists the change.
     *
     * @param instructor the instructor to add
     */
    public void addInstructor(Instructor instructor) {
        instructorRepository.save(instructor);
    }

    /**
     * Returns a copy of the list of all instructors.
     *
     * @return list of instructors
     */
    public List<Instructor> getAllInstructors() {
        return new ArrayList<Instructor>(instructorRepository.findAll());
    }

    /**
     * Retrieves an instructor by identifier.
     *
     * @param id the instructor identifier
     * @return the instructor if found, otherwise {@code null}
     */
    public Instructor getInstructorById(UUID id) {
        return id == null ? null : instructorRepository.findById(id).orElse(null);
    }
}