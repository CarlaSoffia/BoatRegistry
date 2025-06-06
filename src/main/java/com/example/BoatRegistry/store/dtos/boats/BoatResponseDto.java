package com.example.BoatRegistry.store.dtos.boats;

import com.example.BoatRegistry.store.dtos.boatTypes.BoatTypeResponseDto;
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
