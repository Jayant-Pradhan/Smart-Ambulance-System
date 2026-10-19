package com.jayant.hospitalservice.Controller;


import com.jayant.hospitalservice.DTO.DepartmentRequestDTO;
import com.jayant.hospitalservice.DTO.DepartmentResponseDTO;
import com.jayant.hospitalservice.Service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@RequestBody DepartmentRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.createDepartment(requestDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }
    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<DepartmentResponseDTO>> getDepartmentsByHospitalId(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(departmentService.getDepartmentsByHospitalId(hospitalId));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(@PathVariable Long id, @RequestBody DepartmentRequestDTO requestDTO) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, requestDTO));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Boolean> deactivateDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.deactivateDepartment(id));
    }
}
