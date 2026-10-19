package com.jayant.hospitalservice.Kafka.DTO;

import com.jayant.hospitalservice.Enum.BedType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalSelectedEvent {

    private String eventId;
    private Long emergencyId;

    private Long hospitalId;
    private String hospitalName;
    private String hospitalAddress;
    private Double latitude;
    private Double longitude;

    private Long bedId;
    private BedType bedType;

    private LocalDateTime occurredAt;
}