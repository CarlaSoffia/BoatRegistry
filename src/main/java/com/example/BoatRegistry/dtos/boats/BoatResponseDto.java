package com.example.BoatRegistry.dtos.boats;

import com.example.BoatRegistry.dtos.boatTypes.BoatTypeResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoatResponseDto {

    private Long id;

    private String name;

    private String description;

    private Double lengthInMeters;

    private Double widthInMeters;

    private Long builtYear;

    private BoatTypeResponseDto boatType;
}
