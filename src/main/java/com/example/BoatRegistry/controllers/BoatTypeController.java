package com.example.BoatRegistry.controllers;

import com.example.BoatRegistry.services.BoatTypeService;
import com.example.BoatRegistry.store.dtos.BoatTypeResponseDto;
import com.example.BoatRegistry.store.dtos.BoatTypeRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        var boatTypes = boatTypeService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(boatTypes);
    }

    @GetMapping("/boatTypes/{id}")
    public ResponseEntity<BoatTypeResponseDto> getById(@PathVariable Long id) {
        var boatType = boatTypeService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(boatType);
    }

    @PostMapping("/boatTypes")
    public ResponseEntity<BoatTypeResponseDto> create(@Valid @RequestBody BoatTypeRequestDto boatType) {
        var created = boatTypeService.save(boatType);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("boatTypes/{id}")
    public ResponseEntity<BoatTypeResponseDto> update(@PathVariable Long id, @Valid @RequestBody BoatTypeRequestDto boatType) {
        var updated = boatTypeService.update(id, boatType);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("boatTypes/{id}")
    public ResponseEntity<BoatTypeResponseDto> delete(@PathVariable Long id) {
        var boatType = boatTypeService.getById(id);
        boatTypeService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(boatType);
    }
}
