package com.jayant.ambulanceassignmentservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "assignment_emergencies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Emergency {
    @Id
    private Long emergencyId;

    private String emergencyType;

    private Integer patientCount;

    private Double latitude;

    private Double longitude;

    private Boolean unconscious;

    private Boolean breathingDifficulty;

    private String status;

    private Boolean severeBleeding;

    private Boolean trappedInVehicle;

    private LocalDateTime occurredAt;
}
