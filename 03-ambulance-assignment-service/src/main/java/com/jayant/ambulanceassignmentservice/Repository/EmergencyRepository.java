package com.jayant.ambulanceassignmentservice.Repository;

import com.jayant.ambulanceassignmentservice.Entity.Emergency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyRepository extends JpaRepository<Emergency, Long> {
}
