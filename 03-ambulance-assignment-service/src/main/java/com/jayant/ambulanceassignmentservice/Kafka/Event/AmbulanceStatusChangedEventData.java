package com.jayant.ambulanceassignmentservice.Kafka.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmbulanceStatusChangedEventData {
    private Long ambulanceId;
    private String previousStatus;
    private String newStatus;
    private LocalDateTime occurredAt;
}
