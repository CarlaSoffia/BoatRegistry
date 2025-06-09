package com.example.BoatRegistry.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;

@Getter
@Setter
@Table(name = "boat_images")
@Entity
public class BoatImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "media_type", nullable = false)
    private String mediaType;

    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;
}
