package com.example.BoatRegistry.dtos.users;

import com.example.BoatRegistry.validators.NotBlankIfPresent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    @NotBlankIfPresent(message = "Name can not be blank if provided")
    @Size(max = 50, message = "Name can only have 50 characters")
    private String name;

    @NotBlankIfPresent(message = "Email can not be blank if provided")
    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email must be at most 50 characters")
    private String email;

    @NotBlankIfPresent(message = "Password can not be blank if provided")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
