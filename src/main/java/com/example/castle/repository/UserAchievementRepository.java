package com.example.castle.repository;

import com.example.castle.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUserId(Long userId);

    @Query("SELECT COUNT(ua) FROM UserAchievement ua WHERE ua.user.id = :userId")
    int countByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(ua.points), 0) FROM UserAchievement ua WHERE ua.user.id = :userId")
    int sumPointsByUserId(@Param("userId") Long userId);
}