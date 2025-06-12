package com.example.BoatRegistry.controllers;

import com.example.BoatRegistry.dtos.authentication.AuthenticationRequestDto;
import com.example.BoatRegistry.dtos.authentication.AuthenticationResponseDto;
import com.example.BoatRegistry.security.JwtHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

    public AccountController(AuthenticationManager authenticationManager, JwtHelper jwtHelper) {
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@Valid @RequestBody AuthenticationRequestDto authenticationRequestDto) {
        var email = authenticationRequestDto.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, authenticationRequestDto.getPassword()));
        String token = jwtHelper.generateToken(email);
        var authenticationResponseDto = new AuthenticationResponseDto();
        authenticationResponseDto.setEmail(email);
        authenticationResponseDto.setToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponseDto);
    }
}
