package com.jayant.hospitalservice.Kafka.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmbulanceAssignedEvent {
    private String eventId;
    private Long emergencyId;
    private Long ambulanceId;
    private LocalDateTime assignedAt;
}
