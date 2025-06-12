package com.example.BoatRegistry.repositories;

import com.example.BoatRegistry.entities.BoatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoatTypeRepository extends JpaRepository<BoatType, Long> {
    List<BoatType> findByUserEmail(String email);
}
