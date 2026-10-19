package com.jayant.emergencyservice.Repository;

import com.jayant.emergencyservice.Entity.Emergency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyRepository extends JpaRepository<Emergency , Long> {


}
