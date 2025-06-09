package com.example.BoatRegistry.mappers;

import com.example.BoatRegistry.dtos.boatImages.BoatImageRequestDto;
import com.example.BoatRegistry.dtos.boatImages.BoatImageResponseDto;
import com.example.BoatRegistry.entities.BoatImage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BoatImageMapper {

    public BoatImageResponseDto toDto(BoatImage boatImage) {
        var responseDto = new BoatImageResponseDto();
        responseDto.setImage(boatImage.getImage());
        responseDto.setMedia_type(boatImage.getMediaType());
        return responseDto;
    }

    public BoatImage toEntity(BoatImageRequestDto boatImageRequestDto) throws IOException {
        var image = boatImageRequestDto.getImage();
        var boatImage = new BoatImage();
        boatImage.setImage(image.getBytes());
        boatImage.setMediaType(image.getContentType());
        return boatImage;
    }
}
