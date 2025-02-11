package com.example.castle.controller;

import com.example.castle.model.Castle;
import com.example.castle.model.Comment;
import com.example.castle.model.User;
import com.example.castle.service.CastleService;
import com.example.castle.service.CommentService;
import com.example.castle.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CastleService castleService;
    private final UserService userService;

    @PostMapping("/add")
    public String addComment(@RequestParam Long castleId,
                             @RequestParam String content,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        Castle castle = castleService.getCastleById(castleId);
        User user = userService.getUserByUsername(userDetails.getUsername());

        commentService.addComment(castle, user, content);
        redirectAttributes.addFlashAttribute("message", "Комментарий успешно добавлен");

        return "redirect:/castles/" + castleId;
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id,
                                @RequestParam Long castleId,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        Comment comment = commentService.getCommentById(id);
        if (comment.getUser().getUsername().equals(userDetails.getUsername())) {
            commentService.deleteComment(id);
            redirectAttributes.addFlashAttribute("message", "Комментарий удален");
        }
        return "redirect:/castles/" + castleId;
    }
}