package com.example.BoatRegistry.controllers;

import com.example.BoatRegistry.dtos.users.UserRequestDto;
import com.example.BoatRegistry.dtos.users.UserResponseDto;
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

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var user = userService.getById(id, email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/users/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto userRequestDto) {
        var created = userService.save(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/users/{id}/update")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserRequestDto userRequestDto) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var updated = userService.update(id, userRequestDto, email);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/users/{id}/delete")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var user = userService.delete(id, email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
