package com.example.castle.service;

import com.example.castle.model.FunFact;
import com.example.castle.repository.FunFactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FunFactService {
    private final FunFactRepository funFactRepository;

    public List<FunFact> getFunFactsByCastleId(Long castleId) {
        return funFactRepository.findByCastleId(castleId);
    }

    @Transactional
    public FunFact addFunFact(FunFact funFact) {
        return funFactRepository.save(funFact);
    }

    @Transactional
    public void deleteFunFact(Long id) {
        funFactRepository.deleteById(id);
    }

    public FunFact getFunFactById(Long id) {
        return funFactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Интересный факт не найден"));
    }
}