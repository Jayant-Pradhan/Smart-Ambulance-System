package com.jayant.ambulanceassignmentservice.Service;


import com.jayant.ambulanceassignmentservice.Entity.Ambulance;
import com.jayant.ambulanceassignmentservice.Kafka.Event.AmbulanceEventData;
import com.jayant.ambulanceassignmentservice.Kafka.Event.AmbulanceStatusChangedEventData;
import com.jayant.ambulanceassignmentservice.Repository.AmbulanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AmbulanceService {
    private final AmbulanceRepository ambulanceRepository;



    public void saveOrUpdateAmbulance(AmbulanceEventData event) {

        Ambulance ambulance = ambulanceRepository
                .findById(event.getAmbulanceId())
                .orElse(new Ambulance());

        ambulance.setAmbulanceId(event.getAmbulanceId());
        ambulance.setAmbulanceType(event.getAmbulanceType());
        ambulance.setPatientCapacity(event.getPatientCapacity());
        ambulance.setHasOxygen(event.getHasOxygen());
        ambulance.setHasVentilator(event.getHasVentilator());
        ambulance.setHasCardiacMonitor(event.getHasCardiacMonitor());
        ambulance.setLatitude(event.getLatitude());
        ambulance.setLongitude(event.getLongitude());
        ambulance.setStatus(event.getStatus());
        ambulance.setOccurredAt(event.getOccurredAt());

        ambulanceRepository.save(ambulance);
    }

    public void updateAmbulanceStatus(
            AmbulanceStatusChangedEventData event) {

        Ambulance ambulance = ambulanceRepository
                .findById(event.getAmbulanceId())
                .orElseThrow(() -> new RuntimeException(
                        "Ambulance not found: " + event.getAmbulanceId()));

        ambulance.setStatus(event.getNewStatus());
        ambulance.setOccurredAt(event.getOccurredAt());

        ambulanceRepository.save(ambulance);
    }
}
