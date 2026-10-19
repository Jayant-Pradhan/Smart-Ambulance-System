package com.jayant.ambulanceservice.DTO.Update;

import com.jayant.ambulanceservice.Enum.AmbulanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAmbulanceStatusRequest {

    @NotNull(message = "Ambulance status is required")
    private AmbulanceStatus status;

}
