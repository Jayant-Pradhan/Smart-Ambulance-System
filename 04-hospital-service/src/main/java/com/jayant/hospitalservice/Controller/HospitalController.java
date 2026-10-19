package com.jayant.hospitalservice.Controller;

import com.jayant.hospitalservice.DTO.HospitalRequestDTO;
import com.jayant.hospitalservice.DTO.HospitalResponseDTO;
import com.jayant.hospitalservice.Service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;
    @PostMapping
    public ResponseEntity<HospitalResponseDTO> createHospital(@RequestBody HospitalRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hospitalService.createHospital(requestDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponseDTO> getHospitalById(@PathVariable Long id) {
        return ResponseEntity.ok(hospitalService.getHospitalById(id));
    }
    @GetMapping
    public ResponseEntity<List<HospitalResponseDTO>> getAllHospitals() {
        return ResponseEntity.ok(hospitalService.getAllHospitals());
    }
    @PatchMapping("/{id}")
    public ResponseEntity<HospitalResponseDTO> updateHospital(@PathVariable Long id, @RequestBody HospitalRequestDTO requestDTO) {
        return ResponseEntity.ok(hospitalService.updateHospital(id, requestDTO));
    }
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Boolean> deactivateHospital(@PathVariable Long id) {
        return ResponseEntity.ok(hospitalService.deactivateHospital(id));
    }
}
