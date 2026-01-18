package com.example.skipro.controller;

import com.example.skipro.dto.RentalClerkDto;
import com.example.skipro.model.RentalClerk;
import com.example.skipro.service.RentalClerkService;
import com.example.skipro.util.DtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * REST endpoints for rental clerks.
 * <p>
 * This controller exists mainly to support future GUI/extension work. It keeps persistence fully in JPA,
 * without manual SQL.
 * </p>
 */
@RestController
@RequestMapping("/rental-clerks")
public class RentalClerkController {

    private final RentalClerkService rentalClerkService;

    public RentalClerkController(RentalClerkService rentalClerkService) {
        this.rentalClerkService = rentalClerkService;
    }

    /**
     * Creates a new rental clerk.
     *
     * @param yearsOfExperience years of experience
     * @param birthDate         ISO date, e.g. 1990-01-01
     * @param lastName          last name
     * @param firstName         first name
     * @param rentalsHandled    initial rentals handled
     */
    @PostMapping("/create")
    public ResponseEntity<UUID> create(
            @RequestParam int yearsOfExperience,
            @RequestParam String birthDate,
            @RequestParam String lastName,
            @RequestParam String firstName,
            @RequestParam(defaultValue = "0") int rentalsHandled
    ) {
        RentalClerk clerk = new RentalClerk(
                yearsOfExperience,
                LocalDate.parse(birthDate),
                lastName,
                firstName,
                rentalsHandled
        );
        RentalClerk saved = rentalClerkService.addRentalClerk(clerk);
        return ResponseEntity.ok(saved.getId());
    }

    /**
     * Returns all rental clerks.
     */
    @GetMapping
    public List<RentalClerkDto> getAll() {
        return rentalClerkService.getAllRentalClerks().stream()
                .map(DtoMapper::toRentalClerkDto)
                .toList();
    }

    /**
     * Increments handled rentals counter for a given clerk.
     */
    @PostMapping("/{id}/increment")
    public ResponseEntity<Void> increment(@PathVariable String id) {
        rentalClerkService.incrementRentalsHandled(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }
}
