package com.jayant.ambulanceservice.Controller;

import com.jayant.ambulanceservice.DTO.Request.RegisterAmbulanceRequest;
import com.jayant.ambulanceservice.DTO.Response.AmbulanceResponse;
import com.jayant.ambulanceservice.DTO.Update.UpdateAmbulanceLocationRequest;
import com.jayant.ambulanceservice.DTO.Update.UpdateAmbulanceStatusRequest;
import com.jayant.ambulanceservice.Enum.AmbulanceStatus;
import com.jayant.ambulanceservice.Enum.AmbulanceType;
import com.jayant.ambulanceservice.Service.AmbulanceService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AmbulanceControllerTest {
    @Mock
    private AmbulanceService ambulanceService;

    @InjectMocks
    private AmbulanceController ambulanceController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(ambulanceController).build();
        objectMapper = new ObjectMapper();

    }

    @Test
    void  registrationAmbulance_ShouldReturnCreated_WhenRequestIsValid() throws Exception{
        // Arrange
        RegisterAmbulanceRequest request = new RegisterAmbulanceRequest();

        request.setRegistrationNumber("OD02AB1234");
        request.setAmbulanceType(AmbulanceType.BASIC);
        request.setPatientCapacity(2);
        request.setHasOxygen(true);
        request.setHasVentilator(false);
        request.setHasCardiacMonitor(false);

        request.setRegistrationNumber("OD02AB1234");

        AmbulanceResponse response = new AmbulanceResponse();

        when(ambulanceService.registrationAmbulance(any(RegisterAmbulanceRequest.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(post("/api/ambulance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Verify
        verify(ambulanceService).registrationAmbulance(any(RegisterAmbulanceRequest.class));


    }
    @Test
    void getAmbulanceById_ShouldReturnOk_WhenAmbulanceExists() throws Exception {
        // Arrange
        Long id = 1L;
        AmbulanceResponse response = new AmbulanceResponse();

        when(ambulanceService.getAmbulanceById(id))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(get("/api/ambulance/{id}", id))
                .andExpect(status().isOk());

        // Verify
        verify(ambulanceService).getAmbulanceById(id);
    }
    @Test
    void getAllAmbulanceDetails_ShouldReturnOk_WhenRequestIsValid() throws Exception {
        // Arrange
        when(ambulanceService.getAllAmbulances())
                .thenReturn(java.util.List.of(new AmbulanceResponse()));

        // Act + Assert
        mockMvc.perform(get("/api/ambulance"))
                .andExpect(status().isOk());

        // Verify
        verify(ambulanceService).getAllAmbulances();
    }
    @Test
    void updateAmbulanceStatus_ShouldReturnOk_WhenRequestIsValid() throws Exception {
        // Arrange
        Long id = 1L;

        UpdateAmbulanceStatusRequest request = new UpdateAmbulanceStatusRequest();
        request.setStatus(AmbulanceStatus.AVAILABLE);

        AmbulanceResponse response = new AmbulanceResponse();

        when(ambulanceService.updateAmbulanceStatus(id, AmbulanceStatus.AVAILABLE))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(patch("/api/ambulance/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify
        verify(ambulanceService)
                .updateAmbulanceStatus(id, AmbulanceStatus.AVAILABLE);
    }

    @Test
    void updateAmbulanceLocation_ShouldReturnOk_WhenRequestIsValid() throws Exception {
        // Arrange
        Long id = 1L;

        UpdateAmbulanceLocationRequest request =
                new UpdateAmbulanceLocationRequest();

        request.setLatitude(19.0760);
        request.setLongitude(72.8777);

        AmbulanceResponse response = new AmbulanceResponse();

        when(ambulanceService.updateAmbulanceLocation(
                id, 19.0760, 72.8777))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(patch("/api/ambulance/{id}/location", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify
        verify(ambulanceService)
                .updateAmbulanceLocation(id, 19.0760, 72.8777);
    }
}
