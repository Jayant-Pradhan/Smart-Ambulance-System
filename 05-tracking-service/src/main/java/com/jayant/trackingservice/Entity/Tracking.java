package com.jayant.trackingservice.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.jayant.trackingservice.Enum.TripStatus;

@Entity
@Table(name = "tracking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long emergencyId;

    private Long ambulanceId;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    private TripStatus  tripStatus;

    private LocalDateTime updatedAt;
}
