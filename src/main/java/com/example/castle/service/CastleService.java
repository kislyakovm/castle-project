package com.example.castle.service;

import com.example.castle.model.Castle;
import com.example.castle.repository.CastleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CastleService {
    private final CastleRepository castleRepository;

    public List<Castle> getAllCastles() {
        return castleRepository.findAll();
    }

    public Castle getCastleById(Long id) {
        return castleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Castle not found"));
    }

    public List<Castle> getCastlesByCountry(String country) {
        return castleRepository.findByCountry(country);
    }

    public List<String> getAllCountries() {
        return castleRepository.findAllCountries();
    }

    @Transactional
    public Castle saveCastle(Castle castle) {
        return castleRepository.save(castle);
    }

    @Transactional
    public void deleteCastle(Long id) {
        castleRepository.deleteById(id);
    }
}