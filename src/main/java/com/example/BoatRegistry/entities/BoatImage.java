package com.example.BoatRegistry.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "boat_images")
@Entity
public class BoatImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long Id;

    @Lob
    private byte[] image;
}
