package com.example.BoatRegistry.repositories;

import com.example.BoatRegistry.entities.Boat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoatRepository extends JpaRepository<Boat, Long> {
}
