package com.example.BoatRegistry.services;

import com.example.BoatRegistry.dtos.boatTypes.BoatTypeRequestDto;
import com.example.BoatRegistry.dtos.boatTypes.BoatTypeResponseDto;
import com.example.BoatRegistry.entities.BoatType;
import com.example.BoatRegistry.mappers.BoatTypeMapper;
import com.example.BoatRegistry.repositories.BoatImageRepository;
import com.example.BoatRegistry.repositories.BoatRepository;
import com.example.BoatRegistry.repositories.BoatTypeRepository;
import com.example.BoatRegistry.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoatTypeService {

    private final BoatTypeRepository boatTypeRepository;
    private final BoatRepository boatRepository;
    private final BoatImageRepository boatImageRepository;
    private final BoatTypeMapper boatTypeMapper;
    private final UserRepository userRepository;

    public BoatTypeService(BoatTypeRepository boatTypeRepository, BoatRepository boatRepository, BoatImageRepository boatImageRepository, BoatTypeMapper boatTypeMapper, UserRepository userRepository) {
        this.boatTypeRepository = boatTypeRepository;
        this.boatRepository = boatRepository;
        this.boatImageRepository = boatImageRepository;
        this.boatTypeMapper = boatTypeMapper;
        this.userRepository = userRepository;
    }

    public List<BoatTypeResponseDto> getAllByUser(String userEmail) {
       var boatTypes = boatTypeRepository.findByUserEmail(userEmail);
       return boatTypeMapper.toResponseDtoList(boatTypes);
    }

    public BoatTypeResponseDto getById(Long id, String userEmail) {
        var boatType = validateAccessToBoatType(id, userEmail);
        return boatTypeMapper.toResponseDto(boatType);
    }

    public BoatTypeResponseDto save(BoatTypeRequestDto boatTypeRequestDto, String userEmail) {
        validateIfBoatTypeExistsWithName(userEmail, boatTypeRequestDto.getName());

        var boatTypeToInsert = boatTypeMapper.toEntity(boatTypeRequestDto);
        var userOptional = userRepository.findByEmail(userEmail);
        if(userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with email %s not found", userEmail));
        }
        var user = userOptional.get();
        boatTypeToInsert.setUser(user);

        var insertedBoatType =  boatTypeRepository.save(boatTypeToInsert);
        return boatTypeMapper.toResponseDto(insertedBoatType);
    }

    public BoatTypeResponseDto update(Long id, BoatTypeRequestDto boatTypeRequestDto, String userEmail) {
        validateIfBoatTypeExistsWithName(userEmail, boatTypeRequestDto.getName());

        var boatType = validateAccessToBoatType(id, userEmail);
        var newName = boatTypeRequestDto.getName();
        var updatedBoatType = boatType;
        if(!newName.equals(boatType.getName())) {
            boatType.setName(boatTypeRequestDto.getName());
            updatedBoatType = boatTypeRepository.save(boatType);
        }
        return boatTypeMapper.toResponseDto(updatedBoatType);
    }

    public BoatTypeResponseDto delete(Long id, String userEmail) {
        var boatType = validateAccessToBoatType(id, userEmail);

        var boats = boatRepository.findByBoatTypeId(id);
        for(var boat : boats) {
            var boatImage = boat.getBoatImage();
            if(boatImage != null) {
                boatImageRepository.delete(boatImage);
            }
        }
        boatRepository.deleteAll(boats);

        boatTypeRepository.delete(boatType);
        return boatTypeMapper.toResponseDto(boatType);
    }

    private BoatType validateAccessToBoatType(Long id, String userEmail) {
        var boatTypeOptional = boatTypeRepository.findById(id);
        if(boatTypeOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("Boat type with id %s not found", id));
        }
        var boatType = boatTypeOptional.get();

        if(!boatType.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You do not have permission to access this boat type");
        }
        return boatType;
    }

    private void validateIfBoatTypeExistsWithName(String userEmail, String boatTypeName) {
        var boatTypes = boatTypeRepository.findByUserEmail(userEmail);
        for(BoatType boatType : boatTypes) {
            if(boatType.getName().equals(boatTypeName)) {
                throw new DataIntegrityViolationException(String.format("A boat type with name %s already exists", boatTypeName));
            }
        }
    }
}
