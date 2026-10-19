package com.jayant.ambulanceassignmentservice.Kafka.Consumer;


import com.jayant.ambulanceassignmentservice.Kafka.Event.AmbulanceEventData;
import com.jayant.ambulanceassignmentservice.Kafka.Event.AmbulanceStatusChangedEventData;
import com.jayant.ambulanceassignmentservice.Service.AmbulanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmbulanceEventConsumer {

    private final AmbulanceService ambulanceService;


    @KafkaListener(topics = "ambulance.register" , groupId = "Ambulance-Assignment-Group",
            properties = {"spring.json.value.default.type=com.jayant.ambulanceassignmentservice.Kafka.Event.AmbulanceEventData"})
    public void consumingEventFromAmbulance(AmbulanceEventData event){

        log.info("--------  AmbulanceEventData --------------- ");
        log.info("Received AmbulanceEvent: ambulanceId={}, ambulanceType={}, patientCapacity={}, hasOxygen={}, hasVentilator={}, hasCardiacMonitor={}, latitude={}, longitude={}, status={}, occurredAt={}",
                event.getAmbulanceId(),
                event.getAmbulanceType(),
                event.getPatientCapacity(),
                event.getHasOxygen(),
                event.getHasVentilator(),
                event.getHasCardiacMonitor(),
                event.getLatitude(),
                event.getLongitude(),
                event.getStatus(),
                event.getOccurredAt());

        ambulanceService.saveOrUpdateAmbulance(event);

    }
    @KafkaListener(topics =  "ambulance.status.changed", groupId = "Ambulance-Assignment-Group",
            properties = {"spring.json.value.default.type=com.jayant.ambulanceassignmentservice.Kafka.Event.AmbulanceStatusChangedEventData"})
    public void consumingEventFromAmbulanceStatusChanged(AmbulanceStatusChangedEventData event){

        log.info("------------ AmbulanceStatusChangedEventData ------------------ ");
        log.info("Received AmbulanceStatusChangedEvent: ambulanceId={}, previousStatus={}, newStatus={}, occurredAt={}",
                event.getAmbulanceId(),
                event.getPreviousStatus(),
                event.getNewStatus(),
                event.getOccurredAt());

        ambulanceService.updateAmbulanceStatus(event);

    }
}
