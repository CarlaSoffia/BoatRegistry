package com.example.BoatRegistry.controllers;

import com.example.BoatRegistry.services.BoatService;
import com.example.BoatRegistry.store.dtos.boats.BoatCreateRequestDto;
import com.example.BoatRegistry.store.dtos.boats.BoatResponseDto;
import com.example.BoatRegistry.store.dtos.boats.BoatUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoatController {
    private final BoatService boatService;

    public BoatController(BoatService boatService) {
        this.boatService = boatService;
    }

    @GetMapping("/boats")
    public ResponseEntity<List<BoatResponseDto>> getAll() {
        var boats = boatService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(boats);
    }

    @GetMapping("/boats/{id}")
    public ResponseEntity<BoatResponseDto> getById(@PathVariable Long id) {
        var boat = boatService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(boat);
    }

    @PostMapping("/boats")
    public ResponseEntity<BoatResponseDto> create(@RequestBody BoatCreateRequestDto boatCreateRequestDto) {
        var created = boatService.save(boatCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // TODO create update dto request different - cause in update its not mandatory to have every property!
    @PutMapping("/boats/{id}")
    public ResponseEntity<BoatResponseDto> update(@PathVariable Long id, @RequestBody BoatUpdateRequestDto boatUpdateRequestDto) {
        var updated = boatService.update(id, boatUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @DeleteMapping("/boats/{id}")
    public ResponseEntity<BoatResponseDto> delete(@PathVariable Long id) {
        var boat = boatService.getById(id);
        boatService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(boat);
    }
}
