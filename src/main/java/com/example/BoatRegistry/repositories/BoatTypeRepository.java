package com.example.BoatRegistry.repositories;

import com.example.BoatRegistry.entities.BoatType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoatTypeRepository extends JpaRepository<BoatType, Long> {
}
