package com.example.castle.repository;

import com.example.castle.model.Castle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CastleRepository extends JpaRepository<Castle, Long> {
    List<Castle> findByCountry(String country);

    @Query("SELECT DISTINCT c.country FROM Castle c ORDER BY c.country")
    List<String> findAllCountries();

    List<Castle> findByNameContainingIgnoreCase(String name);
}