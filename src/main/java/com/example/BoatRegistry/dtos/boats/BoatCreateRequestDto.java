package com.example.BoatRegistry.dtos.boats;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoatCreateRequestDto {

    @NotBlank(message = "Name is required and cannot be blank")
    @Size(max = 50, message = "Name can only have 50 characters")
    private String name;

    @NotBlank(message = "Description is required and cannot be blank")
    @Size(max = 255, message = "Description can only have 255 characters")
    private String description;

    @NotNull(message = "Length is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Length must be bigger than 0.0 meters")
    private Double lengthInMeters;

    @NotNull(message = "Width is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Width must be bigger than 0.0 meters")
    private Double widthInMeters;

    @NotNull(message = "Built year is required")
    @Min(value = 1, message = "Built year must be bigger than 1")
    private Long builtYear;

    @NotNull(message = "Boat type is required")
    private Long boatTypeId;
}
