package com.jayant.notificationservice.Kafka.DTO;

import com.jayant.notificationservice.Enum.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmbulanceAssignmentEventDTO {
    private Long assignmentId;
    private Long emergencyId;
    private Long ambulanceId;
    private Double distanceInKm;
    private AssignmentStatus assignmentStatus;
    private LocalDateTime assignedAt;
}
