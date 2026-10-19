package com.jayant.trackingservice.DTO;

import com.jayant.trackingservice.Enum.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingResponseDTO {

    private Long id;

    private Long emergencyId;

    private Long ambulanceId;

    private Double latitude;

    private Double longitude;

    private TripStatus tripStatus;

    private LocalDateTime updatedAt;
}