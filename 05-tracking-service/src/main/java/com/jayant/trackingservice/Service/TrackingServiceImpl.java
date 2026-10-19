package com.jayant.trackingservice.Service;

import com.jayant.trackingservice.DTO.TrackingRequestDTO;
import com.jayant.trackingservice.DTO.TrackingResponseDTO;
import com.jayant.trackingservice.Entity.Tracking;
import com.jayant.trackingservice.Enum.TripStatus;
import com.jayant.trackingservice.Exception.TrackingNotFoundException;
import com.jayant.trackingservice.Kafka.DTO.TrackingEventDTO;
import com.jayant.trackingservice.Kafka.Produce.TrackingEventProducer;
import com.jayant.trackingservice.Mapper.TrackingMapper;
import com.jayant.trackingservice.Repository.TrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    private final TrackingRepository trackingRepository;
    private final TrackingEventProducer trackingEventProducer;

    @Override
    public TrackingResponseDTO updateLocation(TrackingRequestDTO requestDTO) {

        Tracking tracking = trackingRepository.save(TrackingMapper.toEntity(requestDTO));

        trackingEventProducer.producingEventFromTrackingService(new TrackingEventDTO(
                tracking.getEmergencyId() , tracking.getAmbulanceId(),
                tracking.getLatitude() , tracking.getLongitude() ,
                tracking.getTripStatus() , tracking.getUpdatedAt()
        ));

        return TrackingMapper.toResponseDTO(tracking);


    }

    @Override
    public TrackingResponseDTO getTrackingByEmergencyId(Long emergencyId) {

         Optional<Tracking> isAvail = trackingRepository.getTrackingByEmergencyId(emergencyId);
         if(isAvail.isPresent()){
             Tracking tracking = isAvail.get();
             return TrackingMapper.toResponseDTO(tracking);
         }

         throw new TrackingNotFoundException("emergency id is not found with this id : " + emergencyId);
    }

    @Override
    public TrackingResponseDTO getTrackingByAmbulanceId(Long ambulanceId) {
        Optional<Tracking> isAvail = trackingRepository.getTrackingByAmbulanceId(ambulanceId);

        if(isAvail.isPresent()){
            Tracking tracking = isAvail.get();
            return TrackingMapper.toResponseDTO(tracking);
        }
        throw new TrackingNotFoundException("ambulance is not found with this ambulance id : " + ambulanceId);
    }

    @Override
    public TrackingResponseDTO updateTripStatus(Long emergencyId, TripStatus tripStatus) {

        Optional<Tracking> isAvail = trackingRepository.getTrackingByEmergencyId(emergencyId);
        if(isAvail.isPresent()){
            Tracking tracking = isAvail.get();
            tracking.setTripStatus(tripStatus);
            tracking.setUpdatedAt(LocalDateTime.now());
            Tracking updatedTracking = trackingRepository.save(tracking);
            trackingEventProducer.producingEventFromTrackingService(
                    new TrackingEventDTO(
                            updatedTracking.getEmergencyId(),
                            updatedTracking.getAmbulanceId(),
                            updatedTracking.getLatitude(),
                            updatedTracking.getLongitude(),
                            updatedTracking.getTripStatus(),
                            updatedTracking.getUpdatedAt()
                    )
            );
            return TrackingMapper.toResponseDTO(updatedTracking);
        }
        throw new TrackingNotFoundException("emergency id is not found with this id : " + emergencyId);
    }

    @Override
    public List<TrackingResponseDTO> getAllTracking() {

        List<TrackingResponseDTO> responseDTOList  = new ArrayList<>();

        List<Tracking> trackingList = trackingRepository.findAll();

        for(Tracking tracking : trackingList){
            responseDTOList.add(TrackingMapper.toResponseDTO(tracking));
        }
        return responseDTOList;
    }
}
