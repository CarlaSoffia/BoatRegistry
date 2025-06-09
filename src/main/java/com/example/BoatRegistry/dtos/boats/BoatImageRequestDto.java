package com.example.BoatRegistry.dtos.boats;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BoatImageRequestDto {
    @NotNull(message = "Image is required")
    private MultipartFile image;
}
