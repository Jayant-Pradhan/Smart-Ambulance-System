package com.jayant.ambulanceassignmentservice.DTO;

import com.jayant.ambulanceassignmentservice.Enum.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentStatusRequestDTO {
    private AssignmentStatus status;
}
