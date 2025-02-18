package com.example.castle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CastleDTO {
    private Long id;
    private String name;
    private String description;
    private String location;
    private String imageUrl;
    private String historicalPeriod;
    private double rating;
    private int visitCount;
    private boolean isFavorite;
    private Date nextPlannedVisit;
    private List<String> features;
    private String openingHours;
    private String ticketInfo;
    private double latitude;
    private double longitude;
}