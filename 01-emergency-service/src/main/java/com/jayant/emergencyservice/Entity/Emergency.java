
package com.jayant.emergencyservice.Entity;

import com.jayant.emergencyservice.Enum.EmergencyType;
import com.jayant.emergencyservice.Enum.EmergencyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "emergencies")
@Getter
@Setter
@NoArgsConstructor
public class Emergency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String callerName;

    @Column(nullable = false, length = 10)
    private String callerMobile;

    @Column(nullable = false, length = 100)
    private String callerEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmergencyType emergencyType;

    @Column(nullable = false)
    private Integer patientCount;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Boolean unconscious;

    private Boolean breathingDifficulty;
    private Boolean severeBleeding;
    private Boolean trappedInVehicle;

    @Column(length = 1000)
    private String additionalDetails;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmergencyStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = EmergencyStatus.CREATED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}