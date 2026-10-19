package com.jayant.ambulanceservice.Kafka.Event;

import com.jayant.ambulanceservice.Enum.AmbulanceStatus;
import com.jayant.ambulanceservice.Enum.AmbulanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmbulanceRegisteredEvent {
    private Long ambulanceId;
    private AmbulanceType ambulanceType;
    private Integer patientCapacity;

    private Boolean hasOxygen;
    private Boolean hasVentilator;
    private Boolean hasCardiacMonitor;

    private Double latitude;
    private Double longitude;

    private AmbulanceStatus status;
    private LocalDateTime occurredAt;
}
