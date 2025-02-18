package com.example.castle.controller;

import com.example.castle.dto.AchievementDTO;
import com.example.castle.dto.CastleDTO;
import com.example.castle.dto.ProfileDTO;
import com.example.castle.dto.ProfileUpdateRequest;
import com.example.castle.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    // Веб-страницы
    @GetMapping
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        ProfileDTO profile = userService.getProfile(userDetails.getUsername());
        List<AchievementDTO> achievements = userService.getUserAchievements(userDetails.getUsername());
        List<CastleDTO> favoriteCastles = userService.getFavoriteCastles(userDetails.getUsername());

        model.addAttribute("profile", profile);
        model.addAttribute("achievements", achievements);
        model.addAttribute("favoriteCastles", favoriteCastles);
        return "profile/view";
    }

    @GetMapping("/edit")
    public String editProfileForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        ProfileDTO profile = userService.getProfile(userDetails.getUsername());
        model.addAttribute("profile", profile);
        return "profile/edit";
    }

    // REST API endpoints
    @GetMapping("/api/current")
    @ResponseBody
    public ResponseEntity<ProfileDTO> getCurrentProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ProfileDTO profile = userService.getProfile(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/api/{username}")
    @ResponseBody
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable String username) {
        ProfileDTO profile = userService.getProfile(username);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/api/update")
    @ResponseBody
    public ResponseEntity<ProfileDTO> updateProfileApi(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProfileUpdateRequest updateRequest) {
        ProfileDTO updatedProfile = userService.updateProfile(userDetails.getUsername(), updateRequest);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/edit")
    public String updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute ProfileUpdateRequest request) {
        userService.updateProfile(userDetails.getUsername(), request);
        return "redirect:/profile";
    }

    @PostMapping("/avatar")
    public String updateAvatar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file) {
        userService.updateAvatar(userDetails.getUsername(), file);
        return "redirect:/profile";
    }

    @GetMapping("/api/achievements")
    @ResponseBody
    public ResponseEntity<List<AchievementDTO>> getUserAchievements(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<AchievementDTO> achievements = userService.getUserAchievements(userDetails.getUsername());
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/api/favorite-castles")
    @ResponseBody
    public ResponseEntity<List<CastleDTO>> getFavoriteCastles(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<CastleDTO> castles = userService.getFavoriteCastles(userDetails.getUsername());
        return ResponseEntity.ok(castles);
    }

    @PostMapping("/api/favorite-castles/{castleId}")
    @ResponseBody
    public ResponseEntity<Void> addToFavorites(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long castleId) {
        userService.addCastleToFavorites(userDetails.getUsername(), castleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/favorite-castles/{castleId}")
    @ResponseBody
    public ResponseEntity<Void> removeFromFavorites(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long castleId) {
        userService.removeCastleFromFavorites(userDetails.getUsername(), castleId);
        return ResponseEntity.ok().build();
    }
}