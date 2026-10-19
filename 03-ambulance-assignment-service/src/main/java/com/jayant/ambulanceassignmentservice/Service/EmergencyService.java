package com.jayant.ambulanceassignmentservice.Service;


import com.jayant.ambulanceassignmentservice.Entity.Emergency;
import com.jayant.ambulanceassignmentservice.Kafka.Event.EmergencyEventData;
import com.jayant.ambulanceassignmentservice.Repository.EmergencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmergencyService {
    private final EmergencyRepository emergencyRepository;

    public void saveOrUpdateEmergency(EmergencyEventData event) {

        Emergency emergency = emergencyRepository.findById(event.getEmergencyId()).orElse(new Emergency());

        emergency.setEmergencyId(event.getEmergencyId());
        emergency.setEmergencyType(event.getEmergencyType());
        emergency.setPatientCount(event.getPatientCount());
        emergency.setLatitude(event.getLatitude());
        emergency.setStatus(event.getStatus());
        emergency.setLongitude(event.getLongitude());
        emergency.setUnconscious(event.getUnconscious());
        emergency.setBreathingDifficulty(event.getBreathingDifficulty());
        emergency.setSevereBleeding(event.getSevereBleeding());
        emergency.setTrappedInVehicle(event.getTrappedInVehicle());
        emergency.setOccurredAt(event.getOccurredAt());

        emergencyRepository.save(emergency);
    }
}
