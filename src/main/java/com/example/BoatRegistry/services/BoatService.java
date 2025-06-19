package com.example.BoatRegistry.services;

import com.example.BoatRegistry.dtos.boatImages.BoatImageResponseDto;
import com.example.BoatRegistry.dtos.boats.BoatCreateRequestDto;
import com.example.BoatRegistry.dtos.boatImages.BoatImageRequestDto;
import com.example.BoatRegistry.dtos.boats.BoatResponseDto;
import com.example.BoatRegistry.dtos.boats.BoatUpdateRequestDto;
import com.example.BoatRegistry.entities.Boat;
import com.example.BoatRegistry.entities.BoatImage;
import com.example.BoatRegistry.entities.BoatType;
import com.example.BoatRegistry.exceptions.ImageUploadException;
import com.example.BoatRegistry.mappers.BoatImageMapper;
import com.example.BoatRegistry.mappers.BoatMapper;
import com.example.BoatRegistry.repositories.BoatImageRepository;
import com.example.BoatRegistry.repositories.BoatRepository;
import com.example.BoatRegistry.repositories.BoatTypeRepository;
import com.example.BoatRegistry.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class BoatService {
    private final BoatRepository boatRepository;
    private final BoatTypeRepository boatTypeRepository;
    private final BoatImageRepository boatImageRepository;
    private final UserRepository userRepository;
    private final BoatMapper boatMapper;
    private final BoatImageMapper boatImageMapper;
    private final String NotFoundBoatImageMessage = "Boat with id %s does not have an image";

    public BoatService(BoatRepository boatRepository, BoatTypeRepository boatTypeRepository, BoatImageRepository boatImageRepository, UserRepository userRepository, BoatMapper boatMapper, BoatImageMapper boatImageMapper) {
        this.boatRepository = boatRepository;
        this.boatTypeRepository = boatTypeRepository;
        this.boatImageRepository = boatImageRepository;
        this.userRepository = userRepository;
        this.boatMapper = boatMapper;
        this.boatImageMapper = boatImageMapper;
    }

    public List<BoatResponseDto> getAll(String userEmail) {
        var boats = boatRepository.findByUserEmail(userEmail);
        return boatMapper.toResponseDtos(boats);
    }

    public BoatResponseDto getById(Long id, String userEmail) {
        var boat = validateAccessToBoat(id, userEmail);
        return boatMapper.toResponseDto(boat);
    }

    public BoatResponseDto save(BoatCreateRequestDto boatCreateRequestDto, String userEmail){
        validateIfBoatExistsWithName(userEmail, boatCreateRequestDto.getName());
        var boat = boatMapper.toEntity(boatCreateRequestDto);
        var userOptional = userRepository.findByEmail(userEmail);
        if(userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with email %s not found", userEmail));
        }
        var user = userOptional.get();
        boat.setUser(user);
        var boatType = getBoatType(boatCreateRequestDto.getBoatTypeId(), userEmail);
        boat.setBoatType(boatType);
        var insertedBoat = boatRepository.save(boat);
        return boatMapper.toResponseDto(insertedBoat);
    }

    public BoatResponseDto update(Long id, BoatUpdateRequestDto boatUpdateRequestDto, String userEmail) {
        validateIfBoatExistsWithName(userEmail, boatUpdateRequestDto.getName());
        var boat = validateAccessToBoat(id, userEmail);
        var newName = boatUpdateRequestDto.getName();
        var newDescription = boatUpdateRequestDto.getDescription();
        var newLength = boatUpdateRequestDto.getLengthInMeters();
        var newWidth = boatUpdateRequestDto.getWidthInMeters();
        var newBuiltYear = boatUpdateRequestDto.getBuiltYear();
        var newBoatType = boatUpdateRequestDto.getBoatTypeId();

        var updateName = newName != null && !newName.equals(boat.getName());
        var updateDescription = newDescription != null && !newDescription.equals(boat.getDescription());
        var updateLength = newLength != null && !newLength.equals(boat.getLengthInMeters());
        var updateWidth = newWidth != null && !newWidth.equals(boat.getWidthInMeters());
        var updateBuiltYear = newBuiltYear != null && !newBuiltYear.equals(boat.getBuiltYear());
        var updateBoatType = newBoatType != null && !newBoatType.equals(boat.getBoatType().getId());

        if(updateName){
            boat.setName(newName);
        }

        if(updateDescription){
            boat.setDescription(newDescription);
        }

        if(updateLength){
            boat.setLengthInMeters(newLength);
        }

        if(updateWidth){
            boat.setWidthInMeters(newWidth);
        }

        if(updateBuiltYear){
            boat.setBuiltYear(newBuiltYear);
        }

        if(updateBoatType){
            var boatType = getBoatType(newBoatType, userEmail);
            boat.setBoatType(boatType);
        }
        var updatedBoat = boat;
        if (updateName || updateDescription || updateLength || updateWidth|| updateBuiltYear || updateBoatType) {
            updatedBoat = boatRepository.save(boat);
        }
        
        return boatMapper.toResponseDto(updatedBoat);
    }

    public BoatResponseDto delete(Long id, String userEmail) {
        var boat = validateAccessToBoat(id, userEmail);
        boatRepository.delete(boat);

        var boatImage = boat.getBoatImage();
        if(boatImage != null) {
            boatImageRepository.delete(boatImage);
        }
        return boatMapper.toResponseDto(boat);
    }

    public BoatResponseDto uploadImage(Long id, BoatImageRequestDto boatImageRequestDto, String userEmail) {
        var boat = validateAccessToBoat(id, userEmail);
        var image = boatImageRequestDto.getImage();
        var contentType = image.getContentType();
        if (contentType == null ||
                !(contentType.equals(MediaType.IMAGE_JPEG_VALUE) || contentType.equals(MediaType.IMAGE_PNG_VALUE))) {
            throw new ImageUploadException("Only JPEG and PNG image types are allowed", null);
        }

        var previousImage = boat.getBoatImage();
        BoatImage boatImage;
        try {
            boatImage = boatImageMapper.toEntity(boatImageRequestDto);

        } catch (IOException e) {
            throw new ImageUploadException(String.format("Error when uploading the image for boat with id %s", id), e);
        }

        boatImageRepository.save(boatImage);
        boat.setBoatImage(boatImage);
        boatRepository.save(boat);

        if(previousImage != null){
            boatImageRepository.delete(previousImage);
        }
        return boatMapper.toResponseDto(boat);
    }


    public BoatResponseDto deleteImage(Long id, String userEmail){
        var boat = validateAccessToBoat(id, userEmail);
        var boatImage = boat.getBoatImage();
        if(boatImage == null) {
            throw new EntityNotFoundException(String.format(NotFoundBoatImageMessage, id));
        }

        boat.setBoatImage(null);
        boatRepository.save(boat);

        boatImageRepository.delete(boatImage);
        return boatMapper.toResponseDto(boat);
    }

    public BoatImageResponseDto getImage(Long id, String userEmail) {
        var boat = validateAccessToBoat(id, userEmail);
        var boatImage = boat.getBoatImage();

        if(boatImage == null) {
            throw new EntityNotFoundException(String.format(NotFoundBoatImageMessage, id));
        }

        return boatImageMapper.toDto(boatImage);
    }

    private BoatType getBoatType(Long boatTypeId, String email) {
        var boatTypeOptional = boatTypeRepository.findById(boatTypeId);

        if(boatTypeOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("Boat type with id %s not found", boatTypeId));
        }
        var boatType = boatTypeOptional.get();

        if(!boatType.getUser().getEmail().equals(email)){
            throw new AccessDeniedException("You do not have permission to create a boat with this boat type");
        }

        return boatType;
    }

    private Boat validateAccessToBoat(Long id, String userEmail) {
        var boatOptional = boatRepository.findById(id);

        if(boatOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("Boat with id %s not found", id));
        }
        var boat = boatOptional.get();

        if(!boat.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You do not have permission to access this boat");
        }
        return boat;
    }

    private void validateIfBoatExistsWithName(String userEmail, String boatName) {
        var boats = boatRepository.findByUserEmail(userEmail);
        for(Boat boat : boats) {
            if(boat.getName().equals(boatName)) {
                throw new DataIntegrityViolationException(String.format("A boat with name %s already exists", boatName));
            }
        }
    }
}
