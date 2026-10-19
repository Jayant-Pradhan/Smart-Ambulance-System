
package com.jayant.ambulanceservice.DTO.Update;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAmbulanceLocationRequest {

    @NotNull(message = "Latitude is required")
    @DecimalMin(
            value = "-90.0",
            message = "Latitude must be at least -90"
    )
    @DecimalMax(
            value = "90.0",
            message = "Latitude must not exceed 90"
    )
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(
            value = "-180.0",
            message = "Longitude must be at least -180"
    )
    @DecimalMax(
            value = "180.0",
            message = "Longitude must not exceed 180"
    )
    private Double longitude;
}
