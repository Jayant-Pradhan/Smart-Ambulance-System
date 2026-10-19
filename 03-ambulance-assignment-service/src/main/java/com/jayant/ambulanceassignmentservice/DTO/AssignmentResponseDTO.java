package com.jayant.ambulanceassignmentservice.DTO;

import com.jayant.ambulanceassignmentservice.Enum.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResponseDTO {

    private Long assignmentId;
    private Long emergencyId;
    private Long ambulanceId;
    private Double distanceInKm;
    private AssignmentStatus assignmentStatus;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
}
