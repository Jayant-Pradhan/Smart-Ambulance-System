package com.jayant.hospitalservice.Mapper;

import com.jayant.hospitalservice.DTO.DepartmentRequestDTO;
import com.jayant.hospitalservice.DTO.DepartmentResponseDTO;
import com.jayant.hospitalservice.Entity.Department;
import com.jayant.hospitalservice.Entity.Hospital;

public class DepartmentMapper {

    public static Department toEntity(DepartmentRequestDTO requestDTO, Hospital hospital) {

        Department department = new Department();

        department.setName(requestDTO.getName());
        department.setHospital(hospital);
        department.setActive(true);

        return department;
    }

    public static DepartmentResponseDTO toResponseDTO(Department department) {

        return DepartmentResponseDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .hospitalId(department.getHospital().getId())
                .hospitalName(department.getHospital().getName())
                .active(department.getActive())
                .build();
    }
}
