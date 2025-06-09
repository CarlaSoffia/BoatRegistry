package com.example.BoatRegistry.services;

import com.example.BoatRegistry.dtos.boatImages.BoatImageResponseDto;
import com.example.BoatRegistry.dtos.boats.BoatCreateRequestDto;
import com.example.BoatRegistry.dtos.boatImages.BoatImageRequestDto;
import com.example.BoatRegistry.dtos.boats.BoatResponseDto;
import com.example.BoatRegistry.dtos.boats.BoatUpdateRequestDto;
import com.example.BoatRegistry.entities.BoatImage;
import com.example.BoatRegistry.entities.BoatType;
import com.example.BoatRegistry.exceptions.ImageUploadException;
import com.example.BoatRegistry.mappers.BoatImageMapper;
import com.example.BoatRegistry.mappers.BoatMapper;
import com.example.BoatRegistry.repositories.BoatImageRepository;
import com.example.BoatRegistry.repositories.BoatRepository;
import com.example.BoatRegistry.repositories.BoatTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class BoatService {
    private final BoatRepository boatRepository;
    private final BoatTypeRepository boatTypeRepository;
    private final BoatImageRepository boatImageRepository;
    private final BoatMapper boatMapper;
    private final BoatImageMapper boatImageMapper;
    private final String NotFoundMessage = "Boat with id %s not found";
    private final String NotFoundBoatTypeMessage = "Boat type with id %s not found";
    private final String NotFoundBoatImageMessage = "Boat with id %s does not have an image";
    private final String ErrorInUploadBoatImageMessage = "Error when uploading the image for boat with id %s";
    private final String InvalidImageType = "Only JPEG and PNG image types are allowed";

    public BoatService(BoatRepository boatRepository, BoatTypeRepository boatTypeRepository, BoatImageRepository boatImageRepository, BoatMapper boatMapper, BoatImageMapper boatImageMapper) {
        this.boatRepository = boatRepository;
        this.boatTypeRepository = boatTypeRepository;
        this.boatImageRepository = boatImageRepository;
        this.boatMapper = boatMapper;
        this.boatImageMapper = boatImageMapper;
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

        var newName = boatUpdateRequestDto.getName();
        var newDescription = boatUpdateRequestDto.getDescription();
        var newLength = boatUpdateRequestDto.getLengthInMeters();
        var newWidth = boatUpdateRequestDto.getWidthInMeters();
        var newBuiltYear = boatUpdateRequestDto.getBuiltYear();
        var newBoatType = boatUpdateRequestDto.getBoatTypeId();

        var updateName = newName != null && !Objects.equals(boat.getName(), newName);
        var updateDescription = newDescription != null && !Objects.equals(boat.getDescription(), newDescription);
        var updateLength = newLength != null && boat.getLengthInMeters() != newLength;
        var updateWidth = newWidth != null && boat.getWidthInMeters() != newWidth;
        var updateBuiltYear = newBuiltYear != null && boat.getBuiltYear() != newBuiltYear;
        var updateBoatType = newBoatType != null && boat.getBoatType().getId() != newBoatType;

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

    public BoatResponseDto uploadImage(Long id, BoatImageRequestDto boatImageRequestDto) {
        var image = boatImageRequestDto.getImage();
        var contentType = image.getContentType();
        if (contentType == null ||
                !(contentType.equals(MediaType.IMAGE_JPEG_VALUE) || contentType.equals(MediaType.IMAGE_PNG_VALUE))) {
            throw new ImageUploadException(InvalidImageType, null);
        }

        var boatOptional = boatRepository.findById(id);
        if(boatOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(NotFoundMessage, id));
        }
        var boat = boatOptional.get();
        var previousImage = boat.getBoatImage();
        if(previousImage != null){
            boatImageRepository.delete(previousImage);
        }

        try {
            var boatImage = boatImageMapper.toEntity(boatImageRequestDto);
            boatImageRepository.save(boatImage);
            boat.setBoatImage(boatImage);
        } catch (IOException e) {
            throw new ImageUploadException(String.format(ErrorInUploadBoatImageMessage, id), e);
        }

        boatRepository.save(boat);
        return boatMapper.toResponseDto(boat);
    }


    public BoatResponseDto deleteImage(Long id){
        var boatOptional = boatRepository.findById(id);
        if(boatOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(NotFoundMessage, id));
        }

        var boat = boatOptional.get();
        var boatImage = boat.getBoatImage();
        if(boatImage == null) {
            throw new EntityNotFoundException(String.format(NotFoundBoatImageMessage, id));
        }

        boat.setBoatImage(null);
        boatRepository.save(boat);

        boatImageRepository.delete(boatImage);
        return boatMapper.toResponseDto(boat);
    }

    public BoatImageResponseDto getImage(Long id) {
        var boatOptional = boatRepository.findById(id);

        if(boatOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(NotFoundMessage, id));
        }
        var boat = boatOptional.get();
        var boatImage = boat.getBoatImage();

        if(boatImage == null) {
            throw new EntityNotFoundException(String.format(NotFoundBoatImageMessage, id));
        }

        return boatImageMapper.toDto(boatImage);
    }

    private BoatType getBoatType(Long boatTypeId) {
        var boatTypeOptional = boatTypeRepository.findById(boatTypeId);

        if(boatTypeOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(NotFoundBoatTypeMessage, boatTypeId));
        }
        return boatTypeOptional.get();
    }


}
