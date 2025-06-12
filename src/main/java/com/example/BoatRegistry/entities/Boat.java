package com.example.BoatRegistry.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "boats")
@Entity
public class Boat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "length_in_meters", nullable = false)
    private Double lengthInMeters;

    @Column(name = "width_in_meters", nullable = false)
    private Double widthInMeters;

    @Column(name = "built_year", nullable = false)
    private Long builtYear;

    @ManyToOne
    @JoinColumn(name = "boat_type_id", nullable = false)
    private BoatType boatType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boat_image_id")
    private BoatImage boatImage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
