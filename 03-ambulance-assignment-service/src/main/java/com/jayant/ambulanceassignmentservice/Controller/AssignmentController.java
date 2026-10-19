package com.jayant.ambulanceassignmentservice.Controller;

import com.jayant.ambulanceassignmentservice.DTO.AssignmentStatusRequestDTO;
import com.jayant.ambulanceassignmentservice.Entity.AmbulanceAssignment;
import com.jayant.ambulanceassignmentservice.Enum.AssignmentStatus;
import com.jayant.ambulanceassignmentservice.Service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    @PostMapping("/emergency/{emergencyId}")
    public ResponseEntity<AmbulanceAssignment> assignAmbulance(@PathVariable Long emergencyId){
        return ResponseEntity.ok(assignmentService.assignAmbulance(emergencyId));
    }

    @GetMapping("/emergency/{emergencyId}")
    public ResponseEntity<AmbulanceAssignment> findAssignmentByEmergencyId(@PathVariable Long emergencyId){
        return ResponseEntity.ok(assignmentService.findAssignmentByEmergencyId(emergencyId));
    }
    @GetMapping("/{assignmentId}")
    public ResponseEntity<AmbulanceAssignment> findAssignmentById(@PathVariable Long assignmentId){
        return ResponseEntity.ok(assignmentService.findAssignmentById(assignmentId));
    }
    @PatchMapping("/{assignmentId}/status")
    public ResponseEntity<AmbulanceAssignment> updateAssignmentStatus(@PathVariable Long assignmentId , @RequestBody AssignmentStatusRequestDTO requestDTO){
        return ResponseEntity.ok(assignmentService.updateAssignmentStatus(assignmentId , requestDTO.getStatus()));
    }
    @GetMapping
    public ResponseEntity<List<AmbulanceAssignment>> getAllAssignments(){
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AmbulanceAssignment>> getAssignmentByStatus(@PathVariable AssignmentStatus status){
        return ResponseEntity.ok(assignmentService.getAssignmentsByStatus(status));
    }




}
