package com.jayant.trackingservice.Service;

import com.jayant.trackingservice.DTO.TrackingRequestDTO;
import com.jayant.trackingservice.DTO.TrackingResponseDTO;
import com.jayant.trackingservice.Enum.TripStatus;

import java.util.List;

public interface TrackingService {

    TrackingResponseDTO updateLocation(TrackingRequestDTO requestDTO);

    TrackingResponseDTO getTrackingByEmergencyId(Long emergencyId);

    TrackingResponseDTO getTrackingByAmbulanceId(Long ambulanceId);

    TrackingResponseDTO updateTripStatus(Long emergencyId, TripStatus tripStatus);

    List<TrackingResponseDTO> getAllTracking();
}
