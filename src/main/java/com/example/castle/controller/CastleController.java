package com.example.castle.controller;

import com.example.castle.model.Castle;
import com.example.castle.service.CastleService;
import com.example.castle.service.CommentService;
import com.example.castle.service.FunFactService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("castle", castle);
        model.addAttribute("comments", commentService.getCommentsByCastleId(id));
        model.addAttribute("funFacts", funFactService.getFunFactsByCastleId(id));
        return "castles/details";
    }

    @GetMapping("/by-country/{country}")
    public String getCastlesByCountry(@PathVariable String country, Model model) {
        model.addAttribute("castles", castleService.getCastlesByCountry(country));
        model.addAttribute("selectedCountry", country);
        model.addAttribute("countries", castleService.getAllCountries());
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
        redirectAttributes.addFlashAttribute("message", "Castle created successfully!");
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
        castle.setId(id);
        castleService.saveCastle(castle);
        redirectAttributes.addFlashAttribute("message", "Castle updated successfully!");
        return "redirect:/castles/" + id;
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCastle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        castleService.deleteCastle(id);
        redirectAttributes.addFlashAttribute("message", "Castle deleted successfully!");
        return "redirect:/";
    }



}