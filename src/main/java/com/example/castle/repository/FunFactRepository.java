package com.example.castle.repository;

import com.example.castle.model.FunFact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FunFactRepository extends JpaRepository<FunFact, Long> {
    List<FunFact> findByCastleId(Long castleId);
}