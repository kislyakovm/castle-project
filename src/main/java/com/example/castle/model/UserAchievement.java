package com.example.castle.model;

import jakarta.persistence.*;
import lombok.Data;
import com.example.castle.model.enums.AchievementType;

import java.util.Date;

@Data
@Entity
@Table(name = "user_achievements")
public class UserAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String name;
    private String description;
    private String iconUrl;
    private int points;
    private boolean unlocked;
    private int progressPercentage;
    private String requirements;

    @Enumerated(EnumType.STRING)
    private AchievementType type;

    @Temporal(TemporalType.TIMESTAMP)
    private Date earnedDate;
}