package com.jayant.notificationservice.Kafka.DTO;


import com.jayant.notificationservice.Enum.EmergencyStatus;
import com.jayant.notificationservice.Enum.EmergencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyEventDTO {

    private String eventId;
    private Long emergencyId;
    private String callerEmail;
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
