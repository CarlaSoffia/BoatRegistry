package com.example.BoatRegistry.store.repositories;

import com.example.BoatRegistry.store.entities.Boat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoatRepository extends JpaRepository<Boat, Long> {
}
