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
public class AmbulanceEventData {
    private Long ambulanceId;
    private String ambulanceType;
    private Integer patientCapacity;

    private Boolean hasOxygen;
    private Boolean hasVentilator;
    private Boolean hasCardiacMonitor;

    private Double latitude;
    private Double longitude;

    private String status;
    private LocalDateTime occurredAt;
}
