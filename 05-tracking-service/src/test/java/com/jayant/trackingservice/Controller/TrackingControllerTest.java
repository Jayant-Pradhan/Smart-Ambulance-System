package com.jayant.trackingservice.Controller;


import com.jayant.trackingservice.DTO.TrackingRequestDTO;
import com.jayant.trackingservice.DTO.TrackingResponseDTO;
import com.jayant.trackingservice.Enum.TripStatus;
import com.jayant.trackingservice.Service.TrackingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;

@WebMvcTest(TrackingController.class)
public class TrackingControllerTest {

    @MockitoBean
    TrackingService trackingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void updateLocation() throws Exception{

        // Arrange :
        TrackingRequestDTO requestDTO = new TrackingRequestDTO(1L,2L,20.2961,85.8245);
        when(trackingService.updateLocation(requestDTO))
                .thenReturn(TrackingResponseDTO.builder()
                                                        .id(1L)
                                                        .emergencyId(1L)
                                                        .ambulanceId(2L)
                                                        .latitude(20.2961)
                                                        .longitude(85.8245)
                                                        .tripStatus(TripStatus.EN_ROUTE)
                                                        .updatedAt(LocalDateTime.now())
                                                        .build());
        mockMvc.perform(post("/api/tracking/location")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(requestDTO)))
                                                                .andExpect(status().isCreated()
        );
    }

    @Test
    public void getTrackingByEmergencyId() throws Exception{
        Long emergencyId = 3L;

        when(trackingService.getTrackingByEmergencyId(emergencyId)).thenReturn(TrackingResponseDTO.builder().id(1L)
                                                                                            .emergencyId(3L)
                                                                                            .ambulanceId(3L)
                                                                                            .latitude(20.2961)
                                                                                            .longitude(85.8245)
                                                                                            .tripStatus(TripStatus.EN_ROUTE)
                                                                                            .updatedAt(LocalDateTime.now())
                                                                                            .build());
        mockMvc.perform(get("/api/tracking/emergency/{emergencyId}",emergencyId)).andExpect(status().isOk())
                                                                                .andExpect(jsonPath("$.emergencyId").value(3))
                                                                                .andExpect(jsonPath("$.ambulanceId").value(3))
                                                                                .andExpect(jsonPath("$.tripStatus").value("EN_ROUTE"));


    }

    @Test
    public void getTrackingByAmbulanceId() throws Exception{
        Long ambulanceId = 5L;
        when(trackingService.getTrackingByAmbulanceId(ambulanceId)).thenReturn(TrackingResponseDTO.builder().id(1L)
                .emergencyId(1L)
                .ambulanceId(5L)
                .latitude(20.2961)
                .longitude(85.8245)
                .tripStatus(TripStatus.EN_ROUTE)
                .updatedAt(LocalDateTime.now()).build());

        mockMvc.perform(get("/api/tracking/ambulance/{ambulanceId}",ambulanceId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.emergencyId").value(1))
                .andExpect(jsonPath("$.ambulanceId").value(5))
                .andExpect(jsonPath("$.tripStatus").value("EN_ROUTE"));

    }

    @Test
    public void updateTripStatus() throws Exception {
        Long emergencyId = 3L;
        TripStatus tripStatus = TripStatus.ARRIVED;

        when(trackingService.updateTripStatus(emergencyId, tripStatus))
                .thenReturn(TrackingResponseDTO.builder()
                        .id(1L)
                        .emergencyId(3L)
                        .ambulanceId(5L)
                        .latitude(20.2961)
                        .longitude(85.8245)
                        .tripStatus(TripStatus.ARRIVED)
                        .updatedAt(LocalDateTime.now())
                        .build());

        mockMvc.perform(patch("/api/tracking/emergency/{emergencyId}/status", emergencyId)
                        .param("tripStatus", tripStatus.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emergencyId").value(3))
                .andExpect(jsonPath("$.tripStatus").value("ARRIVED"));
    }
    @Test
    public void getAllTracking() throws Exception {

        when(trackingService.getAllTracking())
                .thenReturn(List.of(
                        TrackingResponseDTO.builder()
                                .id(1L)
                                .emergencyId(3L)
                                .ambulanceId(5L)
                                .latitude(20.2961)
                                .longitude(85.8245)
                                .tripStatus(TripStatus.EN_ROUTE)
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));

        mockMvc.perform(get("/api/tracking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].emergencyId").value(3))
                .andExpect(jsonPath("$[0].tripStatus").value("EN_ROUTE"));
    }




}
