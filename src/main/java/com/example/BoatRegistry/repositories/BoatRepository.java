package com.example.BoatRegistry.repositories;

import com.example.BoatRegistry.entities.Boat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoatRepository extends JpaRepository<Boat, Long> {
    List<Boat> findByUserEmail(String email);
}
