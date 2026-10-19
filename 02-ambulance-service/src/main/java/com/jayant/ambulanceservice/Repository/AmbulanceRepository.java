package com.jayant.ambulanceservice.Repository;

import com.jayant.ambulanceservice.Entity.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmbulanceRepository extends JpaRepository<Ambulance,Long> {
    boolean existsByRegistrationNumber(String registrationNumber);
}
