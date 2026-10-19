package com.jayant.ambulanceservice.Mapper;

import com.jayant.ambulanceservice.DTO.Request.RegisterAmbulanceRequest;
import com.jayant.ambulanceservice.DTO.Response.AmbulanceResponse;
import com.jayant.ambulanceservice.Entity.Ambulance;
import org.springframework.stereotype.Component;

@Component
public class AmbulanceMapper {

    public Ambulance toEntity(RegisterAmbulanceRequest ambulanceRequest){

        Ambulance ambulance = new Ambulance();

        ambulance.setRegistrationNumber(ambulanceRequest.getRegistrationNumber());
        ambulance.setAmbulanceType(ambulanceRequest.getAmbulanceType());
        ambulance.setPatientCapacity(ambulanceRequest.getPatientCapacity());

        ambulance.setHasOxygen(ambulanceRequest.getHasOxygen());
        ambulance.setHasVentilator(ambulanceRequest.getHasVentilator());
        ambulance.setHasCardiacMonitor(ambulanceRequest.getHasCardiacMonitor());

        ambulance.setLatitude(ambulanceRequest.getLatitude());
        ambulance.setLongitude(ambulanceRequest.getLongitude());

        return ambulance;
    }
    public AmbulanceResponse toResponse(Ambulance ambulance){

        AmbulanceResponse response = new AmbulanceResponse();

        response.setId(ambulance.getId());
        response.setRegistrationNumber(ambulance.getRegistrationNumber());
        response.setAmbulanceType(ambulance.getAmbulanceType());
        response.setPatientCapacity(ambulance.getPatientCapacity());

        response.setHasOxygen(ambulance.getHasOxygen());
        response.setHasVentilator(ambulance.getHasVentilator());
        response.setHasCardiacMonitor(ambulance.getHasCardiacMonitor());

        response.setLatitude(ambulance.getLatitude());
        response.setLongitude(ambulance.getLongitude());

        response.setStatus(ambulance.getStatus());
        response.setCreatedAt(ambulance.getCreatedAt());
        response.setUpdatedAt(ambulance.getUpdatedAt());

        return response;

    }
}
