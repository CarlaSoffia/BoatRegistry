package com.example.BoatRegistry.store.dtos.boatTypes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoatTypeRequestDto {

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name can only have 50 characters")
    private String name;
}
