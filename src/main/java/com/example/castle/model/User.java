package com.example.castle.model;

import com.example.castle.model.enums.AccountStatus;
import com.example.castle.model.enums.UserLevel;
import com.example.castle.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String firstName;
    private String lastName;

    @Column(columnDefinition = "TEXT")
    private String biography;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    private String country;
    private String city;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // добавляем поле role со значением по умолчанию

    private String preferredLanguage;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_castles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "castle_id")
    )
    private Set<Castle> favoriteCastles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<UserAchievement> achievements;

    @Column(name = "reputation_points")
    private Integer reputationPoints = 0;

    @Enumerated(EnumType.STRING)
    private UserLevel level = UserLevel.BEGINNER;

    @Column(name = "last_activity_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActivityDate;
}