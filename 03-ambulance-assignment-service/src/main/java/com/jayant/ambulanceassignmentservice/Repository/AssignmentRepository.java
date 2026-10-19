package com.jayant.ambulanceassignmentservice.Repository;

import com.jayant.ambulanceassignmentservice.Entity.AmbulanceAssignment;
import com.jayant.ambulanceassignmentservice.Enum.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<AmbulanceAssignment, Long> {

    Optional<AmbulanceAssignment> findByEmergencyId(Long EmergencyId);
    Optional<AmbulanceAssignment> findByAmbulanceId(Long ambulanceId);
    List<AmbulanceAssignment> findAllByAssignmentStatus(AssignmentStatus status);
}
