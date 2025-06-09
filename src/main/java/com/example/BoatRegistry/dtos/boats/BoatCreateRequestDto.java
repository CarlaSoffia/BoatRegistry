package com.example.BoatRegistry.dtos.boats;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoatCreateRequestDto {

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name can only have 50 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
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
