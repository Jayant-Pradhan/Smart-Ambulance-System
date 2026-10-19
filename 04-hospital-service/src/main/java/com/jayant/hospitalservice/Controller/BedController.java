package com.jayant.hospitalservice.Controller;


import com.jayant.hospitalservice.DTO.BedRequestDTO;
import com.jayant.hospitalservice.DTO.BedResponseDTO;
import com.jayant.hospitalservice.Enum.BedType;
import com.jayant.hospitalservice.Service.BedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beds")
@RequiredArgsConstructor
public class BedController {

    private final BedService bedService;

    @PostMapping
    public ResponseEntity<BedResponseDTO> createBed(@RequestBody BedRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bedService.createBed(requestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedResponseDTO> getBedById(@PathVariable Long id) {
        return ResponseEntity.ok(bedService.getBedById(id));
    }

    @GetMapping
    public ResponseEntity<List<BedResponseDTO>> getAllBeds() {
        return ResponseEntity.ok(bedService.getAllBeds());
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<BedResponseDTO>> getBedsByHospitalId(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(bedService.getBedsByHospitalId(hospitalId));
    }

    @GetMapping("/hospital/{hospitalId}/available")
    public ResponseEntity<List<BedResponseDTO>> getAvailableBeds(@PathVariable Long hospitalId, @RequestParam BedType bedType) {
        return ResponseEntity.ok(bedService.getAvailableBeds(hospitalId, bedType));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BedResponseDTO> updateBed(@PathVariable Long id, @RequestBody BedRequestDTO requestDTO) {
        return ResponseEntity.ok(bedService.updateBed(id, requestDTO));
    }

    @PatchMapping("/{id}/reserve")
    public ResponseEntity<BedResponseDTO> reserveBed(@PathVariable Long id) {
        return ResponseEntity.ok(bedService.reserveBed(id));
    }

    @PatchMapping("/{id}/occupy")
    public ResponseEntity<BedResponseDTO> occupyBed(@PathVariable Long id) {
        return ResponseEntity.ok(bedService.occupyBed(id));
    }

    @PatchMapping("/{id}/release")
    public ResponseEntity<BedResponseDTO> releaseBed(@PathVariable Long id) {
        return ResponseEntity.ok(bedService.releaseBed(id));
    }

    @PatchMapping("/{id}/out-of-service")
    public ResponseEntity<BedResponseDTO> markBedOutOfService(@PathVariable Long id) {
        return ResponseEntity.ok(bedService.markBedOutOfService(id));
    }

    @PatchMapping("/{id}/available")
    public ResponseEntity<BedResponseDTO> markBedAvailable(@PathVariable Long id) {
        return ResponseEntity.ok(bedService.markBedAvailable(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Boolean> deactivateBed(@PathVariable Long id) {
        return ResponseEntity.ok(bedService.deactivateBed(id));
    }
}
