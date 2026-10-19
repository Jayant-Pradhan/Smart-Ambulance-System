package com.jayant.ambulanceservice.Kafka.Event;

import com.jayant.ambulanceservice.Enum.AmbulanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AmbulanceStatusChangedEvent {
    private Long ambulanceId;
    private AmbulanceStatus previousStatus;
    private AmbulanceStatus newStatus;
    private LocalDateTime occurredAt;
}
