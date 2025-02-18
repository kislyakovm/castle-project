package com.example.castle.dto;

import com.example.castle.model.enums.UserLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    // Базовая информация
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String biography;
    private String country;
    private String city;
    private String avatarUrl;
    private Date registrationDate;
    private Integer reputationPoints;
    private UserLevel level;
    private Date lastActivityDate;

    // Статистика
    private int totalAchievements;
    private int achievementPoints;
    private int totalPlannedVisits;
    private int totalComments;
    private int likesReceived;
    private int visitedCastlesCount;
    private int favoriteCastlesCount;
    private int totalContributions;
}