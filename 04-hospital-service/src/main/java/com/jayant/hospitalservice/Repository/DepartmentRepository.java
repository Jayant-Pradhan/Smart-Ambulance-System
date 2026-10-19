package com.jayant.hospitalservice.Repository;

import com.jayant.hospitalservice.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department , Long> {

    List<Department> findByActiveTrue();
    List<Department> findByHospitalIdAndActiveTrue(Long hospitalId);
}
