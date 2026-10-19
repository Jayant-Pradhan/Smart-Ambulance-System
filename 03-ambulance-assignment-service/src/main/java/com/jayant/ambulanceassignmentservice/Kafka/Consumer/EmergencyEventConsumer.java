package com.jayant.ambulanceassignmentservice.Kafka.Consumer;

import com.jayant.ambulanceassignmentservice.Kafka.Event.EmergencyEventData;
import com.jayant.ambulanceassignmentservice.Service.EmergencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmergencyEventConsumer {
    private final EmergencyService emergencyService;

    @KafkaListener(topics = "emergency.details" , groupId = "Ambulance-Assignment-Group",
            properties = {"spring.json.value.default.type=com.jayant.ambulanceassignmentservice.Kafka.Event.EmergencyEventData"})
    public void consumingEventFromEmergency(EmergencyEventData event){

        log.info("---------- Emergency Publisher Data ---------- ");
        log.info("Received EmergencyEvent: eventId={}, emergencyId={}, emergencyType={}, patientCount={}, latitude={}, longitude={}, unconscious={}, breathingDifficulty={}, severeBleeding={}, trappedInVehicle={}, occurredAt={}",
                event.getEventId(),
                event.getEmergencyId(),
                event.getEmergencyType(),
                event.getPatientCount(),
                event.getLatitude(),
                event.getLongitude(),
                event.getUnconscious(),
                event.getBreathingDifficulty(),
                event.getSevereBleeding(),
                event.getTrappedInVehicle(),
                event.getOccurredAt());

        emergencyService.saveOrUpdateEmergency(event);

    }
}
