package com.example.castle.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.example.castle.model.VisitPlan;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "castles")
public class Castle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    @Column(length = 1000)
    private String description;

    @Column(name = "build_year")
    private Integer buildYear;

    @Column(name = "image_url")
    private String imageUrl;

    private String location;
    private String historicalPeriod;
    private String openingHours;
    private String ticketInfo;

    @Column(nullable = false, columnDefinition = "double precision default 0.0")
    private double longitude;
    @Column(nullable = false, columnDefinition = "double precision default 0.0")
    private double latitude;

    @Column(name = "average_rating", nullable = false, columnDefinition = "double precision default 0.0")
    private double averageRating;

    @Column(name = "visit_count", nullable = false, columnDefinition = "integer default 0")
    private int visitCount;

    @ElementCollection
    @CollectionTable(name = "castle_features", joinColumns = @JoinColumn(name = "castle_id"))
    @Column(name = "feature")
    private List<String> features = new ArrayList<>();

    @OneToMany(mappedBy = "castle", cascade = CascadeType.ALL)
    private List<FunFact> funFacts = new ArrayList<>();

    @OneToMany(mappedBy = "castle", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(mappedBy = "favoriteCastles")
    private Set<User> favoriteByUsers = new HashSet<>();

    @OneToMany(mappedBy = "castle")
    private List<VisitPlan> visits = new ArrayList<>();
}