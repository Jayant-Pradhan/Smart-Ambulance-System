package com.jayant.trackingservice.Mapper;

import com.jayant.trackingservice.DTO.TrackingRequestDTO;
import com.jayant.trackingservice.DTO.TrackingResponseDTO;
import com.jayant.trackingservice.Entity.Tracking;
import com.jayant.trackingservice.Enum.TripStatus;


import java.time.LocalDateTime;

public class TrackingMapper {

    public static Tracking toEntity(TrackingRequestDTO requestDTO) {

        Tracking tracking = new Tracking();

        tracking.setEmergencyId(requestDTO.getEmergencyId());
        tracking.setAmbulanceId(requestDTO.getAmbulanceId());
        tracking.setLatitude(requestDTO.getLatitude());
        tracking.setLongitude(requestDTO.getLongitude());
        tracking.setTripStatus(TripStatus.EN_ROUTE);
        tracking.setUpdatedAt(LocalDateTime.now());

        return tracking;
    }

    public static TrackingResponseDTO toResponseDTO(Tracking tracking) {

        return TrackingResponseDTO.builder()
                .id(tracking.getId())
                .emergencyId(tracking.getEmergencyId())
                .ambulanceId(tracking.getAmbulanceId())
                .latitude(tracking.getLatitude())
                .longitude(tracking.getLongitude())
                .tripStatus(tracking.getTripStatus())
                .updatedAt(tracking.getUpdatedAt())
                .build();
    }
}
