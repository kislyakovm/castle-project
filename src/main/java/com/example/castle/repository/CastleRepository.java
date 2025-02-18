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

    @Query("SELECT COUNT(DISTINCT c) FROM Castle c JOIN c.visits v WHERE v.user.id = :userId AND v.status = 'COMPLETED'")
    int countVisitedByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(DISTINCT c) FROM Castle c JOIN c.favoriteByUsers f WHERE f.id = :userId")
    int countFavoritesByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT c FROM Castle c JOIN c.favoriteByUsers f WHERE f.id = :userId")
    List<Castle> findFavoritesByUserId(@Param("userId") Long userId);
}