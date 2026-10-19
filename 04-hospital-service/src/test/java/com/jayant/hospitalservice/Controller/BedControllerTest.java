package com.jayant.hospitalservice.Controller;

import com.jayant.hospitalservice.Exception.ResourceNotFoundException;
import com.jayant.hospitalservice.Service.BedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BedController.class)
class BedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BedService bedService;

    @Test
    void createBed_shouldReturn201() throws Exception {
        // Arrange
        String requestBody = "{\"bedNumber\":\"B-101\","
                + "\"bedType\":\"ICU\","
                + "\"departmentId\":1}";

        // Act
        ResultActions result = mockMvc.perform(post("/api/beds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Assert
        result.andExpect(status().isCreated());
        verify(bedService).createBed(any());
    }

    @Test
    void createBed_whenDepartmentNotFound_shouldReturn404() throws Exception {
        // Arrange
        String requestBody = "{\"bedNumber\":\"B-101\","
                + "\"bedType\":\"ICU\","
                + "\"departmentId\":99}";
        String errorMessage = "Department not found with id: 99";
        when(bedService.createBed(any()))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        // Act
        ResultActions result = mockMvc.perform(post("/api/beds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void getBedById_shouldReturn200() throws Exception {
        // Arrange
        Long bedId = 1L;

        // Act
        ResultActions result = mockMvc.perform(get("/api/beds/1"));

        // Assert
        result.andExpect(status().isOk());
        verify(bedService).getBedById(bedId);
    }

    @Test
    void getBedById_whenNotFound_shouldReturn404() throws Exception {
        // Arrange
        Long bedId = 99L;
        String errorMessage = "Bed not found with id: 99";
        when(bedService.getBedById(bedId))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        // Act
        ResultActions result = mockMvc.perform(get("/api/beds/99"));

        // Assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void getAllBeds_shouldReturn200() throws Exception {
        // Arrange


        // Act
        ResultActions result = mockMvc.perform(get("/api/beds"));

        // Assert
        result.andExpect(status().isOk());
        verify(bedService).getAllBeds();
    }

    @Test
    void updateBed_shouldReturn200() throws Exception {
        // Arrange
        String requestBody = "{\"bedType\":\"GENERAL\"}";

        // Act
        ResultActions result = mockMvc.perform(patch("/api/beds/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Assert
        result.andExpect(status().isOk());
        verify(bedService).updateBed(any(), any());
    }
}