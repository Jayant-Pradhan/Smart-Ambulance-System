package com.jayant.emergencyservice.DTO.Request;

import com.jayant.emergencyservice.Enum.EmergencyType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmergencyRequest {

    @NotBlank(message = "Caller name is required")
    private String callerName;

    @NotBlank(message = "Caller mobile number is required")
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Enter a valid 10-digit Indian mobile number"
    )
    private String callerMobile;

    private String callerEmail;

    @NotNull(message = "Emergency type is required")
    private EmergencyType emergencyType;

    @NotNull(message = "Patient count is required")
    @Min(value = 1, message = "At least one patient is required")
    private Integer patientCount;

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitude;

    @NotNull(message = "Unconscious status is required")
    private Boolean unconscious;

    private Boolean breathingDifficulty;
    private Boolean severeBleeding;
    private Boolean trappedInVehicle;
    private String additionalDetails;


}
