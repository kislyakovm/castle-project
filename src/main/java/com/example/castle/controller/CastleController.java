package com.example.castle.controller;

import com.example.castle.model.Castle;
import com.example.castle.model.Comment;
import com.example.castle.model.User;
import com.example.castle.service.CastleService;
import com.example.castle.service.CommentService;
import com.example.castle.service.FunFactService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/castles")
@RequiredArgsConstructor
public class CastleController {

    private final CastleService castleService;
    private final CommentService commentService;
    private final FunFactService funFactService;

    @GetMapping("/{id}")
    public String getCastle(@PathVariable Long id, Model model) {
        Castle castle = castleService.getCastleById(id);
        List<Comment> comments = commentService.getCastleComments(id);
        model.addAttribute("castle", castle);
        model.addAttribute("comments", comments);
        model.addAttribute("funFacts", funFactService.getFunFactsByCastleId(id));
        return "castles/details";
    }

    @GetMapping("/by-country/{country}")
    public String getCastlesByCountry(@PathVariable String country, Model model) {
        model.addAttribute("castles", castleService.getCastlesByCountry(country));
        model.addAttribute("country", country);
        return "castles/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createCastleForm(Model model) {
        model.addAttribute("castle", new Castle());
        return "castles/form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createCastle(@ModelAttribute Castle castle, RedirectAttributes redirectAttributes) {
        castleService.saveCastle(castle);
        redirectAttributes.addFlashAttribute("message", "Замок успешно создан");
        return "redirect:/castles/" + castle.getId();
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editCastleForm(@PathVariable Long id, Model model) {
        model.addAttribute("castle", castleService.getCastleById(id));
        return "castles/form";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editCastle(@PathVariable Long id, @ModelAttribute Castle castle, RedirectAttributes redirectAttributes) {
        castleService.updateCastle(id, castle);
        redirectAttributes.addFlashAttribute("message", "Замок успешно обновлен");
        return "redirect:/castles/" + id;
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCastle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        castleService.deleteCastle(id);
        redirectAttributes.addFlashAttribute("message", "Замок успешно удален");
        return "redirect:/";
    }

    @PostMapping("/{castleId}/comments")
    public String addComment(@PathVariable Long castleId,
                             @RequestParam String content,
                             @AuthenticationPrincipal User user,
                             RedirectAttributes redirectAttributes) {
        Castle castle = castleService.getCastleById(castleId);
        commentService.addComment(castle, user, content);
        redirectAttributes.addFlashAttribute("message", "Комментарий успешно добавлен");
        return "redirect:/castles/" + castleId;
    }

    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId,
                                @AuthenticationPrincipal User user,
                                RedirectAttributes redirectAttributes) {
        Comment comment = commentService.getCommentById(commentId);
        Long castleId = comment.getCastle().getId();

        if (comment.getUser().equals(user)) {
            commentService.deleteComment(commentId);
            redirectAttributes.addFlashAttribute("message", "Комментарий удален");
        } else {
            redirectAttributes.addFlashAttribute("error", "У вас нет прав для удаления этого комментария");
        }
        return "redirect:/castles/" + castleId;
    }
}