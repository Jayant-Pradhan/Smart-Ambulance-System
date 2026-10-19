package com.jayant.ambulanceservice.Controller;


import com.jayant.ambulanceservice.DTO.Request.RegisterAmbulanceRequest;
import com.jayant.ambulanceservice.DTO.Response.AmbulanceResponse;
import com.jayant.ambulanceservice.DTO.Update.UpdateAmbulanceLocationRequest;
import com.jayant.ambulanceservice.DTO.Update.UpdateAmbulanceStatusRequest;
import com.jayant.ambulanceservice.Service.AmbulanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ambulance")
@RequiredArgsConstructor
public class AmbulanceController  {
    private final AmbulanceService ambulanceService;

    // registrationAmbulance
    @PostMapping
    public ResponseEntity<AmbulanceResponse> registrationAmbulance(@Valid  @RequestBody  RegisterAmbulanceRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ambulanceService.registrationAmbulance(request));
    }

    // getAmbulanceById(id)
    @GetMapping("/{id}")
    public ResponseEntity<AmbulanceResponse> getAmbulanceById(@PathVariable Long id ){
        return ResponseEntity.ok(ambulanceService.getAmbulanceById(id));
    }

    // getAllAmbulances
    @GetMapping
    public ResponseEntity<List<AmbulanceResponse>> getAllAmbulanceDetails(){
        return ResponseEntity.ok(ambulanceService.getAllAmbulances());
    }

    // updateAmbulanceStatus
    @PatchMapping("/{id}/status")
    public ResponseEntity<AmbulanceResponse> updateAmbulanceStatus(@PathVariable Long id  , @Valid @RequestBody UpdateAmbulanceStatusRequest statusRequest){
        AmbulanceResponse response = ambulanceService.updateAmbulanceStatus(id,statusRequest.getStatus());
        return ResponseEntity.ok(response);
    }
    // updateAmbulanceLocation(id, latitude, longitude)

    @PatchMapping("/{id}/location")
    public ResponseEntity<AmbulanceResponse> updateAmbulanceLocation(@PathVariable  Long id , @Valid @RequestBody UpdateAmbulanceLocationRequest updateRequest){
        AmbulanceResponse response = ambulanceService.updateAmbulanceLocation(id,updateRequest.getLatitude() , updateRequest.getLongitude());
        return ResponseEntity.ok(response);
    }


}
