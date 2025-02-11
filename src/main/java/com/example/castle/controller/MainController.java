package com.example.castle.controller;

import com.example.castle.service.CastleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CastleService castleService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("castles", castleService.getAllCastles());
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}