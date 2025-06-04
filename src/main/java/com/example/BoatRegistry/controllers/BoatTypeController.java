package com.example.BoatRegistry.controllers;

import com.example.BoatRegistry.services.BoatTypeService;
import com.example.BoatRegistry.store.entities.BoatType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoatTypeController {
    private final BoatTypeService boatTypeService;

    public BoatTypeController(BoatTypeService boatTypeService) {
        this.boatTypeService = boatTypeService;
    }

    @GetMapping("/boatTypes")
    public List<BoatType> getAll() {
        return boatTypeService.getAll();
    }

    @GetMapping("/boatTypes/{id}")
    public BoatType getById(@PathVariable Long id) {
        return boatTypeService.getById(id);
    }

    @PostMapping("/boatTypes")
    public BoatType create(@RequestBody BoatType boatType) {
        return boatTypeService.save(boatType);
    }

    @PutMapping("boatTypes/{id}")
    public BoatType update(@PathVariable Long id, @RequestBody BoatType boatType) {
        return boatTypeService.update(id, boatType);
    }

    @DeleteMapping("boatTypes/{id}")
    public void delete(@PathVariable Long id) {
        boatTypeService.delete(id);
    }
}
