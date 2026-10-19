package com.jayant.ambulanceassignmentservice.Controller;

import com.jayant.ambulanceassignmentservice.Entity.AmbulanceAssignment;
import com.jayant.ambulanceassignmentservice.Enum.AssignmentStatus;
import com.jayant.ambulanceassignmentservice.Service.AssignmentService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssignmentController.class)
class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssignmentService assignmentService;

    @Test
    void assignAmbulance_shouldReturn200() throws Exception {

        AmbulanceAssignment assignment = new AmbulanceAssignment();

        when(assignmentService.assignAmbulance(10L))
                .thenReturn(assignment);

        mockMvc.perform(post("/api/assignments/emergency/10"))
                .andExpect(status().isOk());

        verify(assignmentService).assignAmbulance(10L);
    }

    @Test
    void findAssignmentByEmergencyId_shouldReturn200() throws Exception {

        AmbulanceAssignment assignment = new AmbulanceAssignment();

        when(assignmentService.findAssignmentByEmergencyId(10L))
                .thenReturn(assignment);

        mockMvc.perform(get("/api/assignments/emergency/10"))
                .andExpect(status().isOk());

        verify(assignmentService).findAssignmentByEmergencyId(10L);
    }

    @Test
    void findAssignmentById_shouldReturn200() throws Exception {

        AmbulanceAssignment assignment = new AmbulanceAssignment();

        when(assignmentService.findAssignmentById(1L))
                .thenReturn(assignment);

        mockMvc.perform(get("/api/assignments/1"))
                .andExpect(status().isOk());

        verify(assignmentService).findAssignmentById(1L);
    }

    @Test
    void updateAssignmentStatus_shouldReturn200() throws Exception {

        AmbulanceAssignment assignment = new AmbulanceAssignment();

        when(assignmentService.updateAssignmentStatus(1L, AssignmentStatus.ACCEPTED))
                .thenReturn(assignment);

        mockMvc.perform(patch("/api/assignments/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"ACCEPTED\"}"))
                .andExpect(status().isOk());

        verify(assignmentService).updateAssignmentStatus(1L, AssignmentStatus.ACCEPTED);
    }

    @Test
    void getAllAssignments_shouldReturn200() throws Exception {

        when(assignmentService.getAllAssignments())
                .thenReturn(List.of(new AmbulanceAssignment()));

        mockMvc.perform(get("/api/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(assignmentService).getAllAssignments();
    }

    @Test
    void getAssignmentsByStatus_shouldReturn200() throws Exception {

        when(assignmentService.getAssignmentsByStatus(AssignmentStatus.ACCEPTED))
                .thenReturn(List.of(new AmbulanceAssignment()));

        mockMvc.perform(get("/api/assignments/status/ACCEPTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(assignmentService).getAssignmentsByStatus(AssignmentStatus.ACCEPTED);
    }
}