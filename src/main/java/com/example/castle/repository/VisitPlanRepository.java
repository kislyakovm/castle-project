package com.example.castle.repository;

import com.example.castle.model.VisitPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VisitPlanRepository extends JpaRepository<VisitPlan, Long> {
    List<VisitPlan> findByUserIdOrderByPlannedDateAsc(Long userId);

    int countByUserId(Long userId);

    @Query("SELECT MIN(v.plannedDate) FROM VisitPlan v WHERE v.user.id = :userId AND v.castle.id = :castleId AND v.plannedDate >= CURRENT_DATE")
    Optional<Date> findNextVisitDate(@Param("userId") Long userId, @Param("castleId") Long castleId);
}