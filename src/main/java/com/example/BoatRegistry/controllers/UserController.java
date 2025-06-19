package com.example.BoatRegistry.controllers;

import com.example.BoatRegistry.dtos.users.UserCreateRequestDto;
import com.example.BoatRegistry.dtos.users.UserResponseDto;
import com.example.BoatRegistry.dtos.users.UserUpdateRequestDto;
import com.example.BoatRegistry.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserResponseDto> get() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var user = userService.getById(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/users/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserCreateRequestDto userRequestDto) {
        var created = userService.save(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/users/update")
    public ResponseEntity<UserResponseDto> update(@Valid @RequestBody UserUpdateRequestDto userRequestDto) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var updated = userService.update(userRequestDto, email);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/users/delete")
    public ResponseEntity<UserResponseDto> deleteUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var user = userService.delete(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
