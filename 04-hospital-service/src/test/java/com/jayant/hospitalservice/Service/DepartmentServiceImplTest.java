package com.jayant.hospitalservice.Service;

import com.jayant.hospitalservice.DTO.DepartmentRequestDTO;
import com.jayant.hospitalservice.DTO.DepartmentResponseDTO;
import com.jayant.hospitalservice.Entity.Department;
import com.jayant.hospitalservice.Entity.Hospital;
import com.jayant.hospitalservice.Repository.DepartmentRepository;
import com.jayant.hospitalservice.Repository.HospitalRepository;
import com.jayant.hospitalservice.Service.Implementation.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Hospital createHospital(Long id, Boolean active) {
        Hospital hospital = new Hospital();
        hospital.setId(id);
        hospital.setActive(active);
        return hospital;
    }

    private Department createDepartment(Long id, String name, Hospital hospital) {
        Department department = new Department();
        department.setId(id);
        department.setName(name);
        department.setHospital(hospital);
        return department;
    }

    // 1. createDepartment
    @Test
    void createDepartment_shouldSaveAndReturnDepartment() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));
        Department savedDepartment = createDepartment(10L, "Cardiology", hospital);
        when(departmentRepository.save(any(Department.class))).thenReturn(savedDepartment);

        DepartmentRequestDTO requestDTO = new DepartmentRequestDTO();
        requestDTO.setName("Cardiology");
        requestDTO.setHospitalId(1L);

        // Act
        DepartmentResponseDTO actualResult = departmentService.createDepartment(requestDTO);

        // Assert
        assertEquals(10L, actualResult.getId());
        assertEquals("Cardiology", actualResult.getName());
        verify(departmentRepository).save(any(Department.class));
    }

    // 2. getDepartmentById
    @Test
    void getDepartmentById_shouldReturnDepartment() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        Department department = createDepartment(10L, "Cardiology", hospital);
        when(departmentRepository.findById(10L)).thenReturn(Optional.of(department));

        // Act
        DepartmentResponseDTO actualResult = departmentService.getDepartmentById(10L);

        // Assert
        assertEquals(10L, actualResult.getId());
        assertEquals("Cardiology", actualResult.getName());
    }

    // 3. getAllDepartments
    @Test
    void getAllDepartments_shouldReturnActiveDepartments() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        List<Department> departments = new ArrayList<>();
        departments.add(createDepartment(10L, "Cardiology", hospital));
        departments.add(createDepartment(11L, "Neurology", hospital));
        when(departmentRepository.findByActiveTrue()).thenReturn(departments);

        // Act
        List<DepartmentResponseDTO> actualResult = departmentService.getAllDepartments();

        // Assert
        assertEquals(2, actualResult.size());
        assertEquals("Cardiology", actualResult.get(0).getName());
        assertEquals("Neurology", actualResult.get(1).getName());
    }

    // 4. getDepartmentsByHospitalId
    @Test
    void getDepartmentsByHospitalId_shouldReturnDepartments() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));
        List<Department> departments = new ArrayList<>();
        departments.add(createDepartment(10L, "Cardiology", hospital));
        when(departmentRepository.findByHospitalIdAndActiveTrue(1L)).thenReturn(departments);

        // Act
        List<DepartmentResponseDTO> actualResult = departmentService.getDepartmentsByHospitalId(1L);

        // Assert
        assertEquals(1, actualResult.size());
        assertEquals("Cardiology", actualResult.get(0).getName());
    }

    // 5. updateDepartment
    @Test
    void updateDepartment_shouldUpdateName() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        Department department = createDepartment(10L, "Cardiology", hospital);
        when(departmentRepository.findById(10L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentRequestDTO requestDTO = new DepartmentRequestDTO();
        requestDTO.setName("Neurology");

        // Act
        DepartmentResponseDTO actualResult = departmentService.updateDepartment(10L, requestDTO);

        // Assert
        assertEquals("Neurology", actualResult.getName());
        verify(departmentRepository).save(department);
    }

    // 6. deactivateDepartment
    @Test
    void deactivateDepartment_shouldReturnTrueAndSetInactive() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        Department department = createDepartment(10L, "Cardiology", hospital);
        department.setActive(true);
        when(departmentRepository.findById(10L)).thenReturn(Optional.of(department));
        Boolean expectedResult = true;

        // Act
        Boolean actualResult = departmentService.deactivateDepartment(10L);

        // Assert
        assertEquals(expectedResult, actualResult);
        assertEquals(false, department.getActive());
        verify(departmentRepository).save(department);
    }
}