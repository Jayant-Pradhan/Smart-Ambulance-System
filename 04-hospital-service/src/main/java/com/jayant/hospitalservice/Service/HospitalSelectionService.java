package com.jayant.hospitalservice.Service;

import com.jayant.hospitalservice.DTO.HospitalResponseDTO;
import com.jayant.hospitalservice.Kafka.DTO.EmergencyCreatedEvent;

public interface HospitalSelectionService {
    HospitalResponseDTO selectHospital(EmergencyCreatedEvent event);
}
