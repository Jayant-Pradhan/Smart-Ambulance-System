
package com.jayant.ambulanceservice.DTO.Request;

import com.jayant.ambulanceservice.Enum.AmbulanceType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAmbulanceRequest {

    @NotBlank(message = "Registration number is required")
    @Pattern(
            regexp = "^[A-Za-z0-9 -]{4,20}$",
            message = "Registration number must be 4-20 characters"
    )
    private String registrationNumber;

    @NotNull(message = "Ambulance type is required")
    private AmbulanceType ambulanceType;

    @NotNull(message = "Patient capacity is required")
    @Min(value = 1, message = "Patient capacity must be at least 1")
    private Integer patientCapacity;

    @NotNull(message = "Oxygen availability must be specified")
    private Boolean hasOxygen;

    @NotNull(message = "Ventilator availability must be specified")
    private Boolean hasVentilator;

    @NotNull(message = "Cardiac monitor availability must be specified")
    private Boolean hasCardiacMonitor;

    @DecimalMin(value = "-90.0", message = "Latitude must be at least -90")
    @DecimalMax(value = "90.0", message = "Latitude must not exceed 90")
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be at least -180")
    @DecimalMax(value = "180.0", message = "Longitude must not exceed 180")
    private Double longitude;
}
