package com.jayant.emergencyservice.Mapper;

import com.jayant.emergencyservice.DTO.Request.CreateEmergencyRequest;
import com.jayant.emergencyservice.DTO.Response.EmergencyResponse;
import com.jayant.emergencyservice.Entity.Emergency;
import org.springframework.stereotype.Component;

@Component
public class EmergencyMapper {

    public Emergency toEntity(CreateEmergencyRequest request){
        Emergency emergency = new Emergency();

        emergency.setCallerName(request.getCallerName());
        emergency.setCallerMobile(request.getCallerMobile());
        emergency.setCallerEmail(request.getCallerEmail());
        emergency.setEmergencyType(request.getEmergencyType());
        emergency.setPatientCount(request.getPatientCount());
        emergency.setLatitude(request.getLatitude());
        emergency.setLongitude(request.getLongitude());
        emergency.setUnconscious(request.getUnconscious());
        emergency.setBreathingDifficulty(request.getBreathingDifficulty());
        emergency.setSevereBleeding(request.getSevereBleeding());
        emergency.setTrappedInVehicle(request.getTrappedInVehicle());
        emergency.setAdditionalDetails(request.getAdditionalDetails());

        return emergency;

    }

    public EmergencyResponse toResponse(Emergency emergency){

        EmergencyResponse response = new EmergencyResponse();

        response.setId(emergency.getId());
        response.setCallerName(emergency.getCallerName());
        response.setCallerMobile(emergency.getCallerMobile());
        response.setCallerEmail(emergency.getCallerEmail());
        response.setEmergencyType(emergency.getEmergencyType());
        response.setPatientCount(emergency.getPatientCount());
        response.setLatitude(emergency.getLatitude());
        response.setLongitude(emergency.getLongitude());
        response.setUnconscious(emergency.getUnconscious());
        response.setBreathingDifficulty(emergency.getBreathingDifficulty());
        response.setSevereBleeding(emergency.getSevereBleeding());
        response.setTrappedInVehicle(emergency.getTrappedInVehicle());
        response.setAdditionalDetails(emergency.getAdditionalDetails());
        response.setStatus(emergency.getStatus());
        response.setCreatedAt(emergency.getCreatedAt());
        response.setUpdatedAt(emergency.getUpdatedAt());

        return response;

    }
}
