package com.jayant.ambulanceassignmentservice.Entity;

import com.jayant.ambulanceassignmentservice.Enum.AssignmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ambulance_assignments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmbulanceAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @Column(nullable = false, unique = true)
    private Long emergencyId;

    @Column(nullable = false)
    private Long ambulanceId;

    private Double distanceInKm;

    @Enumerated(EnumType.STRING)
    private AssignmentStatus assignmentStatus;

    private LocalDateTime assignedAt;

    private LocalDateTime completedAt;
}