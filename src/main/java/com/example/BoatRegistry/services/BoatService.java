package com.example.BoatRegistry.services;

import com.example.BoatRegistry.store.dtos.boats.BoatCreateRequestDto;
import com.example.BoatRegistry.store.dtos.boats.BoatResponseDto;
import com.example.BoatRegistry.store.dtos.boats.BoatUpdateRequestDto;
import com.example.BoatRegistry.store.entities.BoatType;
import com.example.BoatRegistry.store.mappers.BoatMapper;
import com.example.BoatRegistry.store.repositories.BoatRepository;
import com.example.BoatRegistry.store.repositories.BoatTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BoatService {
    private final BoatRepository boatRepository;
    private final BoatTypeRepository boatTypeRepository;
    private final BoatMapper boatMapper;
    private final String NotFoundMessage = "Boat with id %s not found";
    private final String NotFoundBoatTypeMessage = "Boat type with id %s not found";

    public BoatService(BoatRepository boatRepository, BoatTypeRepository boatTypeRepository, BoatMapper boatMapper) {
        this.boatRepository = boatRepository;
        this.boatTypeRepository = boatTypeRepository;
        this.boatMapper = boatMapper;
    }

    public List<BoatResponseDto> getAll(){
        var boats = boatRepository.findAll();
        return boatMapper.toResponseDtos(boats);
    }

    public BoatResponseDto getById(Long id){
        var boatOptional = boatRepository.findById(id);

        if(boatOptional.isEmpty()){
            throw new EntityNotFoundException(String.format(NotFoundMessage, id));
        }

        var boat = boatOptional.get();
        return boatMapper.toResponseDto(boat);
    }

    public BoatResponseDto save(BoatCreateRequestDto boatCreateRequestDto){
        var boat = boatMapper.toEntity(boatCreateRequestDto);
        var boatType = getBoatType(boatCreateRequestDto.getBoatTypeId());
        boat.setBoatType(boatType);
        var insertedBoat = boatRepository.save(boat);
        return boatMapper.toResponseDto(insertedBoat);
    }

    public BoatResponseDto update(Long id, BoatUpdateRequestDto boatUpdateRequestDto) {
        var boatOptional = boatRepository.findById(id);

        if(boatOptional.isEmpty()){
            throw new EntityNotFoundException(String.format(NotFoundMessage, id));
        }

        var boat = boatOptional.get();
        var updateName = !Objects.equals(boat.getName(), boatUpdateRequestDto.getName());
        var updateDescription = !Objects.equals(boat.getDescription(), boatUpdateRequestDto.getDescription());
        var updateLength = boat.getLengthInMeters() != boatUpdateRequestDto.getLengthInMeters();
        var updateWidth = boat.getWidthInMeters() != boatUpdateRequestDto.getWidthInMeters();
        var updateBuiltYear = boat.getBuiltYear() != boatUpdateRequestDto.getBuiltYear();
        var updateBoatType = boat.getBoatType().getId() != boatUpdateRequestDto.getBoatTypeId();

        if(updateName){
            boat.setName(boatUpdateRequestDto.getName());
        }

        if(updateDescription){
            boat.setDescription(boatUpdateRequestDto.getDescription());
        }

        if(updateLength){
            boat.setLengthInMeters(boatUpdateRequestDto.getLengthInMeters());
        }

        if(updateWidth){
            boat.setWidthInMeters(boatUpdateRequestDto.getWidthInMeters());
        }

        if(updateBuiltYear){
            boat.setBuiltYear(boatUpdateRequestDto.getBuiltYear());
        }

        if(updateBoatType){
            var boatType = getBoatType(boatUpdateRequestDto.getBoatTypeId());
            boat.setBoatType(boatType);
        }
        var updatedBoat = boat;
        if (updateName || updateDescription || updateLength || updateWidth|| updateBuiltYear || updateBoatType) {
            updatedBoat = boatRepository.save(boat);
        }
        
        return boatMapper.toResponseDto(updatedBoat);
    }

    public void delete(Long id) {
        var boatOptional = boatRepository.findById(id);

        if(boatOptional.isEmpty()){
            throw new EntityNotFoundException(String.format(NotFoundMessage, id));
        }

        var boat = boatOptional.get();
        boatRepository.delete(boat);
    }

    private BoatType getBoatType(Long boatTypeId) {
        var boatTypeOptional = boatTypeRepository.findById(boatTypeId);

        if(boatTypeOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(NotFoundBoatTypeMessage, boatTypeId));
        }
        return boatTypeOptional.get();
    }

}
