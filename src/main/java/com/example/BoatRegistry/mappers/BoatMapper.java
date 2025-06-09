package com.example.BoatRegistry.mappers;

import com.example.BoatRegistry.dtos.boats.BoatCreateRequestDto;
import com.example.BoatRegistry.dtos.boats.BoatResponseDto;
import com.example.BoatRegistry.entities.Boat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BoatMapper {

    private final BoatTypeMapper boatTypeMapper;

    public BoatMapper(BoatTypeMapper boatTypeMapper) {
        this.boatTypeMapper = boatTypeMapper;
    }

    public BoatResponseDto toResponseDto(Boat boat) {
        var responseDto = new BoatResponseDto();
        responseDto.setId(boat.getId());
        responseDto.setName(boat.getName());
        responseDto.setDescription(boat.getDescription());
        responseDto.setLengthInMeters(boat.getLengthInMeters());
        responseDto.setWidthInMeters(boat.getWidthInMeters());
        responseDto.setBuiltYear(boat.getBuiltYear());
        responseDto.setBoatType(boatTypeMapper.toResponseDto(boat.getBoatType()));
        return responseDto;
    }

    public List<BoatResponseDto> toResponseDtos(List<Boat> boats) {
        var boatResponseDtos = new ArrayList<BoatResponseDto>();
        for (var boat : boats) {
            var responseDto = toResponseDto(boat);
            boatResponseDtos.add(responseDto);
        }
        return boatResponseDtos;
    }

    public Boat toEntity(BoatCreateRequestDto requestDto) {
        var boat = new Boat();
        boat.setName(requestDto.getName());
        boat.setDescription(requestDto.getDescription());
        boat.setLengthInMeters(requestDto.getLengthInMeters());
        boat.setWidthInMeters(requestDto.getWidthInMeters());
        boat.setBuiltYear(requestDto.getBuiltYear());
        return boat;
    }
}
