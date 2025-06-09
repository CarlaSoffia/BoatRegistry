package com.example.BoatRegistry.dtos.boats;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoatUpdateRequestDto {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name can only have 50 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Length must be bigger than 0.0 meters")
    private Double lengthInMeters;

    @DecimalMin(value = "0.0", inclusive = false, message = "Width must be bigger than 0.0 meters")
    private Double widthInMeters;

    @Min(value = 1, message = "Built year must be bigger than 1")
    private Long builtYear;

    private Long boatTypeId;

}
