package com.example.BoatRegistry.repositories;

import com.example.BoatRegistry.entities.BoatImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoatImageRepository extends JpaRepository<BoatImage, Long> {
}
