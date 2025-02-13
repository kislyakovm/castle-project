package com.example.castle.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "castle", cascade = CascadeType.ALL)
    private List<FunFact> funFacts = new ArrayList<>();

    @OneToMany(mappedBy = "castle", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
}
