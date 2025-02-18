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
public class VisitPlanDTO {
    private Long id;
    private Long castleId;
    private String castleName;
    private Date plannedDate;
    private String status;
    private String notes;
    private int numberOfPeople;
    private String specialRequests;
    private boolean isConfirmed;
    private Date createdAt;
}