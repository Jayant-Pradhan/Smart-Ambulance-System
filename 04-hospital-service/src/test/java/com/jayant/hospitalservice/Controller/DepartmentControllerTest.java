package com.jayant.hospitalservice.Controller;

import com.jayant.hospitalservice.Exception.ResourceNotFoundException;
import com.jayant.hospitalservice.Service.DepartmentService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    @Test
    void createDepartment_shouldReturn201() throws Exception {
        // Arrange
        String requestBody = "{\"name\":\"Cardiology\",\"hospitalId\":1}";

        // Act
        ResultActions result = mockMvc.perform(post("/api/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Assert
        result.andExpect(status().isCreated());
        verify(departmentService).createDepartment(any());
    }

    @Test
    void createDepartment_whenHospitalNotFound_shouldReturn404() throws Exception {
        // Arrange
        String requestBody = "{\"name\":\"Cardiology\",\"hospitalId\":1}";
        String errorMessage = "Hospital not found with id: 1";
        when(departmentService.createDepartment(any()))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        // Act
        ResultActions result = mockMvc.perform(post("/api/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void getDepartmentById_shouldReturn200() throws Exception {
        // Arrange
        Long departmentId = 1L;

        // Act
        ResultActions result = mockMvc.perform(get("/api/departments/1"));

        // Assert
        result.andExpect(status().isOk());
        verify(departmentService).getDepartmentById(departmentId);
    }

    @Test
    void getDepartmentById_whenNotFound_shouldReturn404() throws Exception {
        // Arrange
        Long departmentId = 99L;
        String errorMessage = "Department not found with id: 99";
        when(departmentService.getDepartmentById(departmentId))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        // Act
        ResultActions result = mockMvc.perform(get("/api/departments/99"));

        // Assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void getAllDepartments_shouldReturn200() throws Exception {
        // Arrange
        // kuch setup nahi chahiye, service mock hai

        // Act
        ResultActions result = mockMvc.perform(get("/api/departments"));

        // Assert
        result.andExpect(status().isOk());
        verify(departmentService).getAllDepartments();
    }
}