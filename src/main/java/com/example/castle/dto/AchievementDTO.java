package com.example.castle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementDTO {
    private Long id;
    private String name;
    private String description;
    private String iconUrl;
    private Date earnedDate;
    private int points;
    private String achievementType;
    private boolean isUnlocked;
    private double progressPercentage;
    private String requirements;
}