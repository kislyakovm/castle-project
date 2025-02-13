package com.example.castle.controller;

import com.example.castle.model.Castle;
import com.example.castle.model.FunFact;
import com.example.castle.service.CastleService;
import com.example.castle.service.FunFactService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fun-facts")
@RequiredArgsConstructor
public class FunFactController {
    private final FunFactService funFactService;
    private final CastleService castleService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addFunFact(@RequestParam Long castleId,
                             @RequestParam String fact,
                             RedirectAttributes redirectAttributes) {
        Castle castle = castleService.getCastleById(castleId);
        FunFact funFact = new FunFact();
        funFact.setCastle(castle);
        funFact.setFact(fact);

        funFactService.addFunFact(funFact);
        redirectAttributes.addFlashAttribute("message", "Интересный факт успешно добавлен");

        return "redirect:/castles/" + castleId;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteFunFact(@PathVariable Long id,
                                @RequestParam Long castleId,
                                RedirectAttributes redirectAttributes) {
        funFactService.deleteFunFact(id);
        redirectAttributes.addFlashAttribute("message", "Интересный факт удален");
        return "redirect:/castles/" + castleId;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editFunFact(@PathVariable Long id,
                              @RequestParam String fact,
                              @RequestParam Long castleId,
                              RedirectAttributes redirectAttributes) {
        FunFact funFact = funFactService.getFunFactById(id);
        funFact.setFact(fact);
        funFactService.addFunFact(funFact);
        redirectAttributes.addFlashAttribute("message", "Интересный факт обновлен");
        return "redirect:/castles/" + castleId;
    }
}