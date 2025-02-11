package com.example.castle.repository;

import com.example.castle.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCastleId(Long castleId);
    List<Comment> findByUserId(Long userId);
}