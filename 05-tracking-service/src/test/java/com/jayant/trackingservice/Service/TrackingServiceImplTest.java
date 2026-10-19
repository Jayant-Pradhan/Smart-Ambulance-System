package com.jayant.trackingservice.Service;


import com.jayant.trackingservice.DTO.TrackingRequestDTO;
import com.jayant.trackingservice.DTO.TrackingResponseDTO;
import com.jayant.trackingservice.Entity.Tracking;
import com.jayant.trackingservice.Enum.TripStatus;
import com.jayant.trackingservice.Kafka.Produce.TrackingEventProducer;
import com.jayant.trackingservice.Repository.TrackingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrackingServiceImplTest {

    @Mock
    TrackingRepository trackingRepository;

    @InjectMocks
    TrackingServiceImpl trackingService;

    @Mock
    TrackingEventProducer trackingEventProducer;

    @Test
    public void updateLocation(){
        // Arrange :
        TrackingRequestDTO requestDTO = new TrackingRequestDTO(1L,2L,20.8765 , 85.987);

        Tracking savedTracking = new Tracking();
        savedTracking.setId(1L);
        savedTracking.setEmergencyId(1L);
        savedTracking.setAmbulanceId(2L);
        savedTracking.setLatitude(20.2961);
        savedTracking.setLongitude(85.8245);
        savedTracking.setTripStatus(TripStatus.ASSIGNED);
        savedTracking.setUpdatedAt(LocalDateTime.now());

        when(trackingRepository.save(any(Tracking.class))).thenReturn(savedTracking);

        // ACT :
        TrackingResponseDTO actualResponse = trackingService.updateLocation(requestDTO);

        // Assert
        assertEquals(1L, actualResponse.getId());
        assertEquals(1L, actualResponse.getEmergencyId());
        assertEquals(2L, actualResponse.getAmbulanceId());
        assertEquals(20.2961, actualResponse.getLatitude());
        assertEquals(85.8245, actualResponse.getLongitude());
        assertEquals(TripStatus.ASSIGNED, actualResponse.getTripStatus());

        verify(trackingRepository).save(any(Tracking.class));
        verify(trackingEventProducer).producingEventFromTrackingService(any());
    }

    @Test
    public void getTrackingByEmergencyId() {
        // Arrange
        Long emergencyId = 4L;

        Tracking tracking = new Tracking();
        tracking.setId(1L);
        tracking.setEmergencyId(4L);
        tracking.setAmbulanceId(2L);
        tracking.setLatitude(20.2961);
        tracking.setLongitude(85.8245);
        tracking.setTripStatus(TripStatus.EN_ROUTE);
        tracking.setUpdatedAt(LocalDateTime.now());

        when(trackingRepository.getTrackingByEmergencyId(emergencyId))
                .thenReturn(Optional.of(tracking));

        // Act
        TrackingResponseDTO actualResponse =
                trackingService.getTrackingByEmergencyId(emergencyId);

        // Assert
        assertEquals(4L, actualResponse.getEmergencyId());
        assertEquals(2L, actualResponse.getAmbulanceId());
        assertEquals(TripStatus.EN_ROUTE, actualResponse.getTripStatus());

        verify(trackingRepository).getTrackingByEmergencyId(emergencyId);
    }

    @Test
    public void getTrackingByAmbulanceId(){
        Long ambulanceId = 3L;

        Tracking tracking = new Tracking();
        tracking.setId(1L);
        tracking.setEmergencyId(4L);
        tracking.setAmbulanceId(3L);
        tracking.setLatitude(20.2961);
        tracking.setLongitude(85.8245);
        tracking.setTripStatus(TripStatus.EN_ROUTE);
        tracking.setUpdatedAt(LocalDateTime.now());

        when(trackingRepository.getTrackingByAmbulanceId(ambulanceId)).thenReturn(Optional.of(tracking));

        //Act :
        TrackingResponseDTO actualResponse = trackingService.getTrackingByAmbulanceId(ambulanceId);

        // Assert :
        assertEquals(4L,actualResponse.getEmergencyId());
        assertEquals(3 ,actualResponse.getAmbulanceId());
        verify(trackingRepository).getTrackingByAmbulanceId(ambulanceId);
    }

    @Test
    public void updateTripStatus(){
        Long emergencyId = 4L;
        TripStatus status = TripStatus.ARRIVED;

        Tracking tracking = new Tracking();
        tracking.setId(5L);
        tracking.setEmergencyId(4L);
        tracking.setAmbulanceId(9L);
        tracking.setLatitude(20.2961);
        tracking.setLongitude(85.8245);
        tracking.setTripStatus(TripStatus.EN_ROUTE);
        tracking.setUpdatedAt(LocalDateTime.now());

        when(trackingRepository.getTrackingByEmergencyId(emergencyId))
                .thenReturn(Optional.of(tracking));
        when(trackingRepository.save(any(Tracking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TrackingResponseDTO actualResponse = trackingService.updateTripStatus(emergencyId,status);

        // Assert :
        assertEquals(5,actualResponse.getId());
        assertEquals(4,actualResponse.getEmergencyId());
        assertEquals(TripStatus.ARRIVED, actualResponse.getTripStatus());


        verify(trackingRepository).save(tracking);
        verify(trackingEventProducer)
                .producingEventFromTrackingService(any());
    }
    @Test
    public void getAllTracking() {
        Tracking tracking1 = new Tracking();
        tracking1.setId(5L);
        tracking1.setEmergencyId(4L);
        tracking1.setAmbulanceId(9L);
        tracking1.setLatitude(20.2961);
        tracking1.setLongitude(85.8245);
        tracking1.setTripStatus(TripStatus.EN_ROUTE);
        tracking1.setUpdatedAt(LocalDateTime.now());

        Tracking tracking2 = new Tracking();
        tracking2.setId(6L);
        tracking2.setEmergencyId(7L);
        tracking2.setAmbulanceId(10L);
        tracking2.setLatitude(20.3000);
        tracking2.setLongitude(85.8300);
        tracking2.setTripStatus(TripStatus.ARRIVED);
        tracking2.setUpdatedAt(LocalDateTime.now());

        when(trackingRepository.findAll())
                .thenReturn(List.of(tracking1, tracking2));

        List<TrackingResponseDTO> result = trackingService.getAllTracking();

        // Assert
        assertEquals(2, result.size());

        assertEquals(5L, result.get(0).getId());
        assertEquals(4L, result.get(0).getEmergencyId());
        assertEquals(TripStatus.EN_ROUTE, result.get(0).getTripStatus());

        assertEquals(6L, result.get(1).getId());
        assertEquals(7L, result.get(1).getEmergencyId());
        assertEquals(TripStatus.ARRIVED, result.get(1).getTripStatus());

        verify(trackingRepository).findAll();
    }

    @Test
    public void getAllTracking_whenEmpty_returnsEmptyList() {
        when(trackingRepository.findAll()).thenReturn(List.of());

        List<TrackingResponseDTO> result = trackingService.getAllTracking();

        assertTrue(result.isEmpty());
        verify(trackingRepository).findAll();
    }


}
