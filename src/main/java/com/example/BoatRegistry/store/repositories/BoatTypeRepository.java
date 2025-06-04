package com.example.BoatRegistry.store.repositories;

import com.example.BoatRegistry.store.entities.BoatType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoatTypeRepository extends JpaRepository<BoatType, Long> {
}
