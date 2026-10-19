package com.jayant.emergencyservice.Kafka;

import com.jayant.emergencyservice.Enum.EmergencyStatus;
import com.jayant.emergencyservice.Enum.EmergencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyEvent {

    private String eventId;
    private Long emergencyId;

    private EmergencyType emergencyType;
    private Integer patientCount;

    private Double latitude;
    private Double longitude;



    private EmergencyStatus status;

    private String callerEmail;
    private Boolean unconscious;
    private Boolean breathingDifficulty;
    private Boolean severeBleeding;
    private Boolean trappedInVehicle;

    private LocalDateTime occurredAt;
}
