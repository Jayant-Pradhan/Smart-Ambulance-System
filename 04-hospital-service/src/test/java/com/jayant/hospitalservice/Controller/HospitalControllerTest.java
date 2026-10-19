package com.jayant.hospitalservice.Controller;

import com.jayant.hospitalservice.Exception.ResourceNotFoundException;
import com.jayant.hospitalservice.Service.HospitalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HospitalController.class)
class HospitalControllerTest {

    private static final String BASE_URL = "/api/hospitals";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HospitalService hospitalService;

    @Test
    @DisplayName("POST /api/hospitals returns 201 for a valid request")
    void createHospital_shouldReturn201() throws Exception {
        // Arrange
        var requestBody = """
                {
                  "name": "City Care Hospital",
                  "address": "Main Road, Berhampur",
                  "city": "Berhampur",
                  "latitude": 19.3149,
                  "longitude": 84.7941
                }
                """;

        // Act
        var result = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Assert
        result.andExpect(status().isCreated());
        verify(hospitalService).createHospital(any());
    }

    @Test
    @DisplayName("GET /api/hospitals/{id} returns 200")
    void getHospitalById_shouldReturn200() throws Exception {
        // Arrange
        var hospitalId = 1L;

        // Act
        var result = mockMvc.perform(get(BASE_URL + "/{id}", hospitalId));

        // Assert
        result.andExpect(status().isOk());
        verify(hospitalService).getHospitalById(hospitalId);
    }

    @Test
    @DisplayName("GET /api/hospitals/{id} returns 404 when hospital does not exist")
    void getHospitalById_whenNotFound_shouldReturn404() throws Exception {
        // Arrange
        var hospitalId = 99L;
        var errorMessage = "Hospital not found with id: " + hospitalId;
        when(hospitalService.getHospitalById(hospitalId))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        // Act
        var result = mockMvc.perform(get(BASE_URL + "/{id}", hospitalId));

        // Assert
        result.andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404)).andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    @DisplayName("GET /api/hospitals returns 200")
    void getAllHospitals_shouldReturn200() throws Exception {
        // Arrange (no setup needed, service is mocked)

        // Act
        var result = mockMvc.perform(get(BASE_URL));

        // Assert
        result.andExpect(status().isOk());
        verify(hospitalService).getAllHospitals();
    }

    @Test
    @DisplayName("PATCH /api/hospitals/{id} returns 200 for a partial update")
    void updateHospital_shouldReturn200() throws Exception {
        // Arrange
        var hospitalId = 1L;
        var requestBody = """
                {
                  "city": "Bhubaneswar"
                }
                """;

        // Act
        var result = mockMvc.perform(patch(BASE_URL + "/{id}", hospitalId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Assert
        result.andExpect(status().isOk());
        verify(hospitalService).updateHospital(any(), any());
    }

    @Test
    @DisplayName("PATCH /api/hospitals/{id}/deactivate returns 200 when deactivation succeeds")
    void deactivateHospital_shouldReturn200() throws Exception {
        // Arrange
        var hospitalId = 1L;
        when(hospitalService.deactivateHospital(hospitalId)).thenReturn(true);

        // Act
        var result = mockMvc.perform(patch(BASE_URL + "/{id}/deactivate", hospitalId));

        // Assert
        result.andExpect(status().isOk());
        verify(hospitalService).deactivateHospital(hospitalId);
    }
}