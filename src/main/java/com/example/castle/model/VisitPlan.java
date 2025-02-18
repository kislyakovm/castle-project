package com.example.castle.model;

import com.example.castle.model.enums.VisitStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "visit_plans")
public class VisitPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Castle castle;

    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedDate;

    @Enumerated(EnumType.STRING)
    private VisitStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "number_of_people")
    private int numberOfPeople;

    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;

    private boolean isConfirmed;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}