package com.jayant.notificationservice.Kafka.DTO;

import com.jayant.notificationservice.Enum.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingEventDTO {
    private Long emergencyId;
    private Long ambulanceId;
    private Double latitude;
    private Double longitude;
    private TripStatus tripStatus;
    private LocalDateTime updatedAt;
}
