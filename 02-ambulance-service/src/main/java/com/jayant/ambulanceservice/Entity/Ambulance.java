
package com.jayant.ambulanceservice.Entity;

import com.jayant.ambulanceservice.Enum.AmbulanceStatus;
import com.jayant.ambulanceservice.Enum.AmbulanceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "ambulances",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_ambulance_registration_number",
                        columnNames = "registration_number"
                )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ambulance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Registration number is required")
    @Pattern(
            regexp = "^[A-Za-z0-9 -]{4,20}$",
            message = "Registration number must be 4-20 characters"
    )
    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @NotNull(message = "Ambulance type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AmbulanceType ambulanceType;

    @NotNull(message = "Patient capacity is required")
    @Min(value = 1, message = "Patient capacity must be at least 1")
    @Column(nullable = false)
    private Integer patientCapacity;

    @NotNull(message = "Oxygen availability must be specified")
    @Column(nullable = false)
    private Boolean hasOxygen;

    @NotNull(message = "Ventilator availability must be specified")
    @Column(nullable = false)
    private Boolean hasVentilator;

    @NotNull(message = "Cardiac monitor availability must be specified")
    @Column(nullable = false)
    private Boolean hasCardiacMonitor;

    @DecimalMin(value = "-90.0", message = "Latitude must be at least -90")
    @DecimalMax(value = "90.0", message = "Latitude must not exceed 90")
    @Column
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be at least -180")
    @DecimalMax(value = "180.0", message = "Longitude must not exceed 180")
    @Column
    private Double longitude;

    @NotNull(message = "Ambulance status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AmbulanceStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = AmbulanceStatus.OFFLINE;
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
