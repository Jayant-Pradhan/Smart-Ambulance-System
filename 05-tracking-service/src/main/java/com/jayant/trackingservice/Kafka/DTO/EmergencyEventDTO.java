package com.jayant.trackingservice.Kafka.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyEventDTO {
    private Long emergencyId;
    private String emergencyType;
    private Double latitude;
    private Double longitude;
    private String status;
}
