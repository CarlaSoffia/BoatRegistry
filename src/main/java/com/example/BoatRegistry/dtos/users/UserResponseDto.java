package com.example.BoatRegistry.dtos.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;

    private String name;

    private String email;
}
