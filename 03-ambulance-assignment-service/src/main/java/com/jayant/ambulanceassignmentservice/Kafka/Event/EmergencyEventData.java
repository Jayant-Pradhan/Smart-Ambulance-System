package com.jayant.ambulanceassignmentservice.Kafka.Event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyEventData {
    private String eventId;
    private Long emergencyId;

    private String emergencyType;
    private Integer patientCount;

    private Double latitude;
    private Double longitude;
    private String status;

    private Boolean unconscious;
    private Boolean breathingDifficulty;
    private Boolean severeBleeding;
    private Boolean trappedInVehicle;

    private LocalDateTime occurredAt;
}
