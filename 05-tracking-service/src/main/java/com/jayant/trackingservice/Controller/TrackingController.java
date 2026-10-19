package com.jayant.trackingservice.Controller;

import com.jayant.trackingservice.DTO.TrackingRequestDTO;
import com.jayant.trackingservice.DTO.TrackingResponseDTO;
import com.jayant.trackingservice.Enum.TripStatus;
import com.jayant.trackingservice.Service.TrackingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tracking")
public class    TrackingController {

    private final TrackingService trackingService;

    @PostMapping("/location")
    public ResponseEntity<TrackingResponseDTO> updateLocation(@Valid @RequestBody TrackingRequestDTO requestDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(trackingService.updateLocation(requestDTO));
    }

    @GetMapping("/emergency/{emergencyId}")
    public ResponseEntity<TrackingResponseDTO> getTrackingByEmergencyId(@PathVariable Long emergencyId) {

        return ResponseEntity.ok(trackingService.getTrackingByEmergencyId(emergencyId));
    }

    @GetMapping("/ambulance/{ambulanceId}")
    public ResponseEntity<TrackingResponseDTO> getTrackingByAmbulanceId(@PathVariable Long ambulanceId) {

        return ResponseEntity.ok(trackingService.getTrackingByAmbulanceId(ambulanceId));
    }

    @PatchMapping("/emergency/{emergencyId}/status")
    public ResponseEntity<TrackingResponseDTO> updateTripStatus(@PathVariable Long emergencyId, @RequestParam TripStatus tripStatus) {

        return ResponseEntity.ok(trackingService.updateTripStatus(emergencyId, tripStatus));
    }

    @GetMapping
    public ResponseEntity<List<TrackingResponseDTO>> getAllTracking() {

        return ResponseEntity.ok(trackingService.getAllTracking());
    }


}
