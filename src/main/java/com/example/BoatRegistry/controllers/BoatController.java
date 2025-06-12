package com.example.BoatRegistry.controllers;

import com.example.BoatRegistry.dtos.boatImages.BoatImageRequestDto;
import com.example.BoatRegistry.services.BoatService;
import com.example.BoatRegistry.dtos.boats.BoatCreateRequestDto;
import com.example.BoatRegistry.dtos.boats.BoatResponseDto;
import com.example.BoatRegistry.dtos.boats.BoatUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoatController {
    private final BoatService boatService;

    public BoatController(BoatService boatService) {
        this.boatService = boatService;
    }

    @GetMapping("/boats")
    public ResponseEntity<List<BoatResponseDto>> getAll() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var boats = boatService.getAll(email);
        return ResponseEntity.status(HttpStatus.OK).body(boats);
    }

    @GetMapping("/boats/{id}")
    public ResponseEntity<BoatResponseDto> getById(@PathVariable Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var boat = boatService.getById(id, email);
        return ResponseEntity.status(HttpStatus.OK).body(boat);
    }

    @PostMapping("/boats")
    public ResponseEntity<BoatResponseDto> create(@Valid @RequestBody BoatCreateRequestDto boatCreateRequestDto) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var created = boatService.save(boatCreateRequestDto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/boats/{id}")
    public ResponseEntity<BoatResponseDto> update(@PathVariable Long id, @Valid @RequestBody BoatUpdateRequestDto boatUpdateRequestDto) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var updated = boatService.update(id, boatUpdateRequestDto, email);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/boats/{id}")
    public ResponseEntity<BoatResponseDto> delete(@PathVariable Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var boat = boatService.delete(id, email);
        return ResponseEntity.status(HttpStatus.OK).body(boat);
    }

    @PutMapping("/boats/{id}/upload/image")
    public ResponseEntity<BoatResponseDto> submitImage(@PathVariable Long id, @Valid BoatImageRequestDto boatImageRequestDto) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var boat = boatService.uploadImage(id, boatImageRequestDto, email);
        return ResponseEntity.status(HttpStatus.OK).body(boat);
    }

    @GetMapping("/boats/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var boatImageResponseDto = boatService.getImage(id, email);
        var image = boatImageResponseDto.getImage();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(boatImageResponseDto.getMedia_type()));
        headers.setContentLength(image.length);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    @DeleteMapping("/boats/{id}/delete/image")
    public ResponseEntity<BoatResponseDto> deleteImage(@PathVariable Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var boat = boatService.deleteImage(id, email);
        return ResponseEntity.status(HttpStatus.OK).body(boat);
    }
}
