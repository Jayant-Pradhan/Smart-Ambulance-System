package com.jayant.emergencyservice.Service;

import com.jayant.emergencyservice.DTO.Request.CreateEmergencyRequest;
import com.jayant.emergencyservice.DTO.Response.EmergencyResponse;

import java.util.List;

public interface EmergencyService {

    EmergencyResponse createEmergency(CreateEmergencyRequest request);

    EmergencyResponse getEmergencyDetailsUsingId(Long id);

    List<EmergencyResponse> getAllEmergencyDetails();

    EmergencyResponse cancelEmergency(Long id);

    EmergencyResponse getEmergencyStatus(Long id);

}
