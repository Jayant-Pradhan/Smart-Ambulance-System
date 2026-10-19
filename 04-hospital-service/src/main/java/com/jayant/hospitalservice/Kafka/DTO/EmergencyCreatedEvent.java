package com.jayant.hospitalservice.Kafka.DTO;

import com.jayant.hospitalservice.Enum.EmergencyStatus;
import com.jayant.hospitalservice.Enum.EmergencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyCreatedEvent {
    private String eventId;
    private Long emergencyId;

    private EmergencyType emergencyType;
    private Integer patientCount;

    private Double latitude;
    private Double longitude;

    private EmergencyStatus status;

    private Boolean unconscious;
    private Boolean breathingDifficulty;
    private Boolean severeBleeding;
    private Boolean trappedInVehicle;

    private LocalDateTime occurredAt;
}
