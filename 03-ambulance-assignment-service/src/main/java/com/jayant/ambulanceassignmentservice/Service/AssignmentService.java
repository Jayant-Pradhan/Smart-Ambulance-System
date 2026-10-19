package com.jayant.ambulanceassignmentservice.Service;

import com.jayant.ambulanceassignmentservice.Entity.AmbulanceAssignment;
import com.jayant.ambulanceassignmentservice.Enum.AssignmentStatus;

import java.util.List;

public interface AssignmentService {

    AmbulanceAssignment assignAmbulance(Long emergencyId);
    AmbulanceAssignment findAssignmentByEmergencyId(Long emergencyId);
    AmbulanceAssignment findAssignmentById(Long assignmentId);
    AmbulanceAssignment updateAssignmentStatus(Long assignmentId , AssignmentStatus status);
    List<AmbulanceAssignment> getAllAssignments();
    List<AmbulanceAssignment> getAssignmentsByStatus(AssignmentStatus status);
}
