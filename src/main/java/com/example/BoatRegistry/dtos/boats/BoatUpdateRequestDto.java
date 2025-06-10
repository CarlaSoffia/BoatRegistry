package com.example.BoatRegistry.dtos.boats;

import com.example.BoatRegistry.validators.NotBlankIfPresent;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoatUpdateRequestDto {

    @NotBlankIfPresent(message = "Name cannot not be blank if provided")
    @Size(max = 50, message = "Name can only have 50 characters")
    private String name;

    @NotBlankIfPresent(message = "Description cannot not be blank if provided")
    @Size(max = 255, message = "Description can only have 255 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Length must be bigger than 0.0 meters")
    private Double lengthInMeters;

    @DecimalMin(value = "0.0", inclusive = false, message = "Width must be bigger than 0.0 meters")
    private Double widthInMeters;

    @Min(value = 1, message = "Built year must be bigger than 1")
    private Long builtYear;

    private Long boatTypeId;

}
