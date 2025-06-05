package com.example.BoatRegistry.services;

import com.example.BoatRegistry.store.dtos.BoatTypeRequestDto;
import com.example.BoatRegistry.store.dtos.BoatTypeResponseDto;
import com.example.BoatRegistry.store.mappers.BoatTypeMapper;
import com.example.BoatRegistry.store.repositories.BoatTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoatTypeService {

    private final BoatTypeRepository boatTypeRepository;
    private final BoatTypeMapper boatTypeMapper;
    private final String NotFoundMessage = "Boat type with id %s not found";

    public BoatTypeService(BoatTypeRepository boatTypeRepository, BoatTypeMapper boatTypeMapper) {
        this.boatTypeRepository = boatTypeRepository;
        this.boatTypeMapper = boatTypeMapper;
    }

    public List<BoatTypeResponseDto> getAll() {
       var boatTypes = boatTypeRepository.findAll();
       return boatTypeMapper.toResponseDtoList(boatTypes);
    }

    public BoatTypeResponseDto getById(Long id) {
       var boatTypeOptional = boatTypeRepository.findById(id);

       if(boatTypeOptional.isEmpty()) {
           throw new EntityNotFoundException(String.format(NotFoundMessage, id));
       }

       var boatType = boatTypeOptional.get();
       return boatTypeMapper.toResponseDto(boatType);
    }

    public BoatTypeResponseDto save(BoatTypeRequestDto boatTypeRequestDto) {
        var boatTypeToInsert = boatTypeMapper.toEntity(boatTypeRequestDto);
        var insertedBoatType =  boatTypeRepository.save(boatTypeToInsert);
        return boatTypeMapper.toResponseDto(insertedBoatType);
    }

    public BoatTypeResponseDto update(Long id, BoatTypeRequestDto boatTypeRequestDto) {
        var boatTypeOptional = boatTypeRepository.findById(id);

        if(boatTypeOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(NotFoundMessage, id));
        }
        var boatType = boatTypeOptional.get();
        boatType.setName(boatTypeRequestDto.getName());
        var updatedBoatType = boatTypeRepository.save(boatType);
        return boatTypeMapper.toResponseDto(updatedBoatType);
    }

    public void delete(Long id) {
        var boatTypeOptional = boatTypeRepository.findById(id);

        if(boatTypeOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(NotFoundMessage, id));
        }
        var boatType = boatTypeOptional.get();
        boatTypeRepository.delete(boatType);
    }
}
