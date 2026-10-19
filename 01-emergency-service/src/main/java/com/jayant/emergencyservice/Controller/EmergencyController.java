package com.jayant.emergencyservice.Controller;

import com.jayant.emergencyservice.DTO.Request.CreateEmergencyRequest;
import com.jayant.emergencyservice.DTO.Response.EmergencyResponse;
import com.jayant.emergencyservice.Service.EmergencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api/emergency")
public class EmergencyController {

    private final EmergencyService emergencyService;

    @PostMapping
    public ResponseEntity<EmergencyResponse> createEmergency(@Valid  @RequestBody CreateEmergencyRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(emergencyService.createEmergency(request));

    }

    // get emergency details using id
    @GetMapping("/{id}")
    public ResponseEntity<EmergencyResponse> getEmergencyDetailsUsingId(@PathVariable Long id){
        return ResponseEntity.ok(emergencyService.getEmergencyDetailsUsingId(id));
    }

    // get all Emergency details
    @GetMapping
    public ResponseEntity<List<EmergencyResponse>> getAllEmergencies(){
        return ResponseEntity.ok(emergencyService.getAllEmergencyDetails());
    }

    // Cancel Emergency
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<EmergencyResponse> cancelEmergency(@PathVariable Long id ){
        return ResponseEntity.ok(emergencyService.cancelEmergency(id));
    }

    // Get Emergency Status
    @GetMapping("/{id}/status")
    public ResponseEntity<EmergencyResponse> getEmergencyStatus(@PathVariable Long id ){
        return ResponseEntity.ok(emergencyService.getEmergencyStatus(id));
    }





}
