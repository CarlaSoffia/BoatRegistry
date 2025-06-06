package com.example.BoatRegistry.services;

import com.example.BoatRegistry.store.dtos.BoatRequestDto;
import com.example.BoatRegistry.store.dtos.BoatResponseDto;
import com.example.BoatRegistry.store.entities.Boat;
import com.example.BoatRegistry.store.entities.BoatType;
import com.example.BoatRegistry.store.mappers.BoatMapper;
import com.example.BoatRegistry.store.mappers.BoatTypeMapper;
import com.example.BoatRegistry.store.repositories.BoatRepository;
import com.example.BoatRegistry.store.repositories.BoatTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public BoatResponseDto save(BoatRequestDto boatRequestDto){
        var boat = boatMapper.toEntity(boatRequestDto);
        var boatType = getBoatType(boatRequestDto);
        boat.setBoatType(boatType);
        var insertedBoat = boatRepository.save(boat);
        return boatMapper.toResponseDto(insertedBoat);
    }

    public BoatResponseDto update(Long id, BoatRequestDto boatRequestDto) {
        var boatOptional = boatRepository.findById(id);

        if(boatOptional.isEmpty()){
            throw new EntityNotFoundException(String.format(NotFoundMessage, id));
        }

        var boat = boatOptional.get();

        boat.setName(boatRequestDto.getName());
        boat.setDescription(boatRequestDto.getDescription());
        boat.setLengthInMeters(boatRequestDto.getLengthInMeters());
        boat.setWidthInMeters(boatRequestDto.getWidthInMeters());
        boat.setBuiltYear(boatRequestDto.getBuiltYear());
        var boatType = getBoatType(boatRequestDto);
        boat.setBoatType(boatType);

        var updatedBoat = boatRepository.save(boat);
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

    private BoatType getBoatType(BoatRequestDto boatRequestDto) {
        var boatTypeId = boatRequestDto.getBoatTypeId();
        var boatTypeOptional = boatTypeRepository.findById(boatTypeId);

        if(boatTypeOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(NotFoundBoatTypeMessage, boatTypeId));
        }
        return boatTypeOptional.get();
    }

}
