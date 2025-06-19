package com.example.BoatRegistry.mappers;

import com.example.BoatRegistry.dtos.boatTypes.BoatTypeResponseDto;
import com.example.BoatRegistry.entities.Boat;
import com.example.BoatRegistry.entities.BoatType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@MockitoSettings
class BoatMapperTest {


    @Mock
    private BoatTypeMapper boatTypeMapper;

    @InjectMocks
    private BoatMapper  boatMapper;

    // Add your test methods here
    // Example test method
    @Test
    void testToResponseDto() {
        // Given
        // Create a Boat object and set its properties
        final var boat = new Boat();
        final var boatType = new BoatType();
        boat.setId(1L);
        boat.setBoatType(boatType);

        when(boatTypeMapper.toResponseDto(boatType)).thenReturn(new BoatTypeResponseDto());

        // When
        // Call the toResponseDto method with the Boat object
        boatMapper.toResponseDto(boat);
        // Then
        // Assert that the returned BoatResponseDto has the expected properties
        assertNotNull(boat.getId());
    }


}