package com.example.BoatRegistry.services;

import com.example.BoatRegistry.store.entities.BoatType;
import com.example.BoatRegistry.store.repositories.BoatTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoatTypeService {

    private final BoatTypeRepository boatTypeRepository;

    public BoatTypeService(BoatTypeRepository boatTypeRepository) {
        this.boatTypeRepository = boatTypeRepository;
    }

    public List<BoatType> getAll() {
       return boatTypeRepository.findAll();
    }

    public BoatType getById(Long id) {
        return boatTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BoatType with id " + id + " not found"));
    }

    public BoatType save(BoatType boatType) {
        // validations here or in the controller ???
        return boatTypeRepository.save(boatType);
    }

    public BoatType update(Long id, BoatType boatType) {
        var boatTypeToUpdate = boatTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BoatType with id " + id + " not found"));

        boatTypeToUpdate.setName(boatType.getName());
        return boatTypeRepository.save(boatTypeToUpdate);
    }

    public void delete(Long id) {
        var boatType = boatTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BoatType with id " + id + " not found"));
        boatTypeRepository.delete(boatType);
    }
}
