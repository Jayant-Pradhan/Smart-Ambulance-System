package com.jayant.hospitalservice.Service.Implementation;

import com.jayant.hospitalservice.DTO.DepartmentRequestDTO;
import com.jayant.hospitalservice.DTO.DepartmentResponseDTO;
import com.jayant.hospitalservice.Entity.Department;
import com.jayant.hospitalservice.Entity.Hospital;
import com.jayant.hospitalservice.Exception.ResourceNotFoundException;
import com.jayant.hospitalservice.Mapper.DepartmentMapper;
import com.jayant.hospitalservice.Repository.DepartmentRepository;
import com.jayant.hospitalservice.Repository.HospitalRepository;
import com.jayant.hospitalservice.Service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final HospitalRepository hospitalRepository;
    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO) {
        Optional<Hospital> isAvail = hospitalRepository.findById(requestDTO.getHospitalId());
        if(isAvail.isPresent()){
            Hospital hospital = isAvail.get();
            if(hospital.getActive() == true){
                return DepartmentMapper.toResponseDTO(departmentRepository.save(DepartmentMapper.toEntity(requestDTO,hospital)));
            }
        }
        throw new ResourceNotFoundException("Hospital not found with id: " + requestDTO.getHospitalId());
    }
    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Optional<Department> isAvail =  departmentRepository.findById(id);

        if(isAvail.isPresent()){
            Department department = isAvail.get();
            return  DepartmentMapper.toResponseDTO(department);
        }
        throw new ResourceNotFoundException("department id is not found with this id : " +  id);
    }
    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {

        List<DepartmentResponseDTO> responseDTOList = new ArrayList<>();

        List<Department> response = departmentRepository.findByActiveTrue();

        for(Department department : response){
            responseDTOList.add(DepartmentMapper.toResponseDTO(department));
        }
        return responseDTOList;
    }
    @Override
    public List<DepartmentResponseDTO> getDepartmentsByHospitalId(Long hospitalId) {
        List<DepartmentResponseDTO> responseDTOList = new ArrayList<>();

        Optional<Hospital> isAvail = hospitalRepository.findById(hospitalId);

        if(isAvail.isPresent()){
            List<Department> departmentList = departmentRepository.findByHospitalIdAndActiveTrue(hospitalId);

            for(Department department : departmentList){
                responseDTOList.add(DepartmentMapper.toResponseDTO(department));
            }
        }
        else{
            throw new ResourceNotFoundException("hospital is not found with this id : " + hospitalId);
        }


        return responseDTOList;
    }
    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO requestDTO) {

        Optional<Department> isAvail = departmentRepository.findById(id);
        if(isAvail.isPresent()){
            Department department = isAvail.get();
            if (requestDTO.getName() != null) {
                department.setName(requestDTO.getName());
            }

            return DepartmentMapper.toResponseDTO(
                    departmentRepository.save(department)
            );
        }

        throw new ResourceNotFoundException(
                "Department not found with id: " + id
        );


    }
    @Override
    public Boolean deactivateDepartment(Long id) {

        Optional<Department> isAvail = departmentRepository.findById(id);

        if(isAvail.isPresent()){
            Department department  = isAvail.get();
            department.setActive(false);
            departmentRepository.save(department);
            return true;

        }
        throw new ResourceNotFoundException("department  is not found with his id : " +  id);
    }
}
