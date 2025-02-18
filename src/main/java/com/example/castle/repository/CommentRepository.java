package com.example.castle.repository;

import com.example.castle.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCastleId(Long castleId);
    List<Comment> findByUserId(Long userId);

    // Изменяем возвращаемый тип с long на int
    int countByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(c.likes), 0) FROM Comment c WHERE c.user.id = :userId")
    int sumLikesByUserId(@Param("userId") Long userId);
}