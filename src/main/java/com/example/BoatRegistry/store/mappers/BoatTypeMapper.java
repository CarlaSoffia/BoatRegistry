package com.example.BoatRegistry.store.mappers;

import com.example.BoatRegistry.store.dtos.boatTypes.BoatTypeRequestDto;
import com.example.BoatRegistry.store.dtos.boatTypes.BoatTypeResponseDto;
import com.example.BoatRegistry.store.entities.BoatType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BoatTypeMapper {

    public BoatTypeResponseDto toResponseDto(BoatType boatType) {
        var boatTypeResponseDto = new BoatTypeResponseDto();
        boatTypeResponseDto.setId(boatType.getId());
        boatTypeResponseDto.setName(boatType.getName());
        return boatTypeResponseDto;
    }

    public List<BoatTypeResponseDto> toResponseDtoList(List<BoatType> boatTypes) {
        var boatTypeResponseDtos = new ArrayList<BoatTypeResponseDto>();
        for (BoatType boatType : boatTypes) {
            var boatTypeResponseDto = toResponseDto(boatType);
            boatTypeResponseDtos.add(boatTypeResponseDto);
        }
        return boatTypeResponseDtos;
    }

    public BoatType toEntity(BoatTypeRequestDto boatTypeRequestDto) {
        var boatType = new BoatType();
        boatType.setName(boatTypeRequestDto.getName());
        return boatType;
    }
}
