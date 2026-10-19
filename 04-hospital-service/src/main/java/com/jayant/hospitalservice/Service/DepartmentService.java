package com.jayant.hospitalservice.Service;

import com.jayant.hospitalservice.DTO.DepartmentRequestDTO;
import com.jayant.hospitalservice.DTO.DepartmentResponseDTO;

import java.util.List;

public interface DepartmentService {

    DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO);

    DepartmentResponseDTO getDepartmentById(Long id);

    List<DepartmentResponseDTO> getAllDepartments();

    List<DepartmentResponseDTO> getDepartmentsByHospitalId(Long hospitalId);

    DepartmentResponseDTO updateDepartment(Long id , DepartmentRequestDTO requestDTO);

    Boolean deactivateDepartment(Long id);

}
