
package com.jayant.emergencyservice.DTO.Response;

import com.jayant.emergencyservice.Enum.EmergencyType;
import com.jayant.emergencyservice.Enum.EmergencyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyResponse {

    private Long id;

    private String callerName;

    private String callerMobile;
    private String callerEmail;

    private EmergencyType emergencyType;

    private Integer patientCount;

    private Double latitude;

    private Double longitude;

    private Boolean unconscious;

    private Boolean breathingDifficulty;

    private Boolean severeBleeding;

    private Boolean trappedInVehicle;

    private String additionalDetails;

    private EmergencyStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}