package com.jayant.ambulanceassignmentservice.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_ambulances")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ambulance {
    @Id
    private Long ambulanceId;

    @Column(nullable = false)
    private String ambulanceType;

    @Column(nullable = false)
    private Integer patientCapacity;

    private Boolean hasOxygen;

    private Boolean hasVentilator;

    private Boolean hasCardiacMonitor;

    private Double latitude;

    private Double longitude;

    @Column(nullable = false)
    private String status;

    private LocalDateTime occurredAt;
}
