package com.example.BoatRegistry.controllers;

import com.example.BoatRegistry.services.BoatTypeService;
import com.example.BoatRegistry.dtos.boatTypes.BoatTypeResponseDto;
import com.example.BoatRegistry.dtos.boatTypes.BoatTypeRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoatTypeController {
    private final BoatTypeService boatTypeService;

    public BoatTypeController(BoatTypeService boatTypeService) {
        this.boatTypeService = boatTypeService;
    }

    @GetMapping("/boatTypes")
    public ResponseEntity<List<BoatTypeResponseDto>> getAll() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var boatTypes = boatTypeService.getAllByUser(email);
        return ResponseEntity.status(HttpStatus.OK).body(boatTypes);
    }

    @GetMapping("/boatTypes/{id}")
    public ResponseEntity<BoatTypeResponseDto> getById(@PathVariable Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var boatType = boatTypeService.getById(id, email);
        return ResponseEntity.status(HttpStatus.OK).body(boatType);
    }

    @PostMapping("/boatTypes")
    public ResponseEntity<BoatTypeResponseDto> create(@Valid @RequestBody BoatTypeRequestDto boatType) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var created = boatTypeService.save(boatType, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/boatTypes/{id}")
    public ResponseEntity<BoatTypeResponseDto> update(@PathVariable Long id, @Valid @RequestBody BoatTypeRequestDto boatType) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var updated = boatTypeService.update(id, boatType, email);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/boatTypes/{id}")
    public ResponseEntity<BoatTypeResponseDto> delete(@PathVariable Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var boatType = boatTypeService.delete(id, email);
        return ResponseEntity.status(HttpStatus.OK).body(boatType);
    }
}
