package com.example.castle.service;

import com.example.castle.model.Castle;
import com.example.castle.model.Comment;
import com.example.castle.model.User;
import com.example.castle.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getCastleComments(Long castleId) {
        return commentRepository.findByCastleId(castleId);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));
    }

    @Transactional
    public Comment addComment(Castle castle, User user, String content) {
        Comment comment = new Comment();
        comment.setCastle(castle);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}