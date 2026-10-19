package com.jayant.ambulanceservice.DTO.Response;

import com.jayant.ambulanceservice.Enum.AmbulanceStatus;
import com.jayant.ambulanceservice.Enum.AmbulanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmbulanceResponse {
    private Long id;
    private String registrationNumber;
    private AmbulanceType ambulanceType;
    private Integer patientCapacity;

    private Boolean hasOxygen;
    private Boolean hasVentilator;
    private Boolean hasCardiacMonitor;

    private Double latitude;
    private Double longitude;

    private AmbulanceStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
