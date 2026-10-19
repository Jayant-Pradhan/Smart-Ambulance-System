package com.jayant.ambulanceservice.Service;

import com.jayant.ambulanceservice.DTO.Request.RegisterAmbulanceRequest;
import com.jayant.ambulanceservice.DTO.Response.AmbulanceResponse;
import com.jayant.ambulanceservice.Enum.AmbulanceStatus;

import java.util.List;

public interface AmbulanceService {

    AmbulanceResponse registrationAmbulance(RegisterAmbulanceRequest ambulanceRequest);
    AmbulanceResponse getAmbulanceById(Long id);
    List<AmbulanceResponse> getAllAmbulances();
    AmbulanceResponse updateAmbulanceStatus(Long id , AmbulanceStatus status);
    AmbulanceResponse updateAmbulanceLocation(Long id , Double latitude , Double longitude);


}
