package com.example.BoatRegistry.controllers;

import com.example.BoatRegistry.services.BoatService;
import com.example.BoatRegistry.dtos.boats.BoatCreateRequestDto;
import com.example.BoatRegistry.dtos.boats.BoatResponseDto;
import com.example.BoatRegistry.dtos.boats.BoatUpdateRequestDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class BoatController {
    private final BoatService boatService;

    public BoatController(BoatService boatService) {
        this.boatService = boatService;
    }

    @GetMapping("/boats")
    public ResponseEntity<List<BoatResponseDto>> getAll() {
        var boats = boatService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(boats);
    }

    @GetMapping("/boats/{id}")
    public ResponseEntity<BoatResponseDto> getById(@PathVariable Long id) {
        var boat = boatService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(boat);
    }

    @PostMapping("/boats")
    public ResponseEntity<BoatResponseDto> create(@RequestBody BoatCreateRequestDto boatCreateRequestDto) {
        var created = boatService.save(boatCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/boats/{id}")
    public ResponseEntity<BoatResponseDto> update(@PathVariable Long id, @RequestBody BoatUpdateRequestDto boatUpdateRequestDto) {
        var updated = boatService.update(id, boatUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @DeleteMapping("/boats/{id}")
    public ResponseEntity<BoatResponseDto> delete(@PathVariable Long id) {
        var boat = boatService.getById(id);
        boatService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(boat);
    }

    @PutMapping("boats/{id}/upload/image")
    public ResponseEntity<BoatResponseDto> submitImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        var boat = boatService.uploadImage(id, image);
        return ResponseEntity.status(HttpStatus.OK).body(boat);
    }

    @GetMapping("boats/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        var image = boatService.getImage(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Or IMAGE_PNG, etc.
        headers.setContentLength(image.length);

        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    @DeleteMapping("boats/{id}/delete/image")
    public ResponseEntity<BoatResponseDto> deleteImage(@PathVariable Long id) {
        var boat = boatService.deleteImage(id);
        return ResponseEntity.status(HttpStatus.OK).body(boat);
    }
}
