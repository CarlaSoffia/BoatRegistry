package com.example.BoatRegistry.dtos.boatImages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoatImageResponseDto {
    private byte[] image;
    private String media_type;
}
