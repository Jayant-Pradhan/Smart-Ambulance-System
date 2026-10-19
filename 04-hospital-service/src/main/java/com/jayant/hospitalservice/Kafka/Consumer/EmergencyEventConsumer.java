package com.jayant.hospitalservice.Kafka.Consumer;


import com.jayant.hospitalservice.Kafka.DTO.AmbulanceAssignedEvent;
import com.jayant.hospitalservice.Kafka.DTO.EmergencyCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;




@Service
@Slf4j
public class EmergencyEventConsumer {



    @KafkaListener(topics = "emergency.details" , groupId = "hospital-group", properties = {
            "spring.json.value.default.type=com.jayant.hospitalservice.Kafka.DTO.EmergencyCreatedEvent"
    })
    public void consumingEventFromEmergencyService(EmergencyCreatedEvent emergency){
        log.info(" ---------- Emergency Service Details -------------- ");
        log.info("event Id : {} " , emergency.getEventId() );
        log.info("emergency Id : {} " , emergency.getEmergencyId());
        log.info("emergencyType : {} " , emergency.getEmergencyType());
        log.info("patientCount : {} " , emergency.getPatientCount());
        log.info("latitude : {} " , emergency.getLatitude());
        log.info("longitude : {} " , emergency.getLongitude());
        log.info("status : {} ", emergency.getStatus());
        log.info("unconscious : {} " , emergency.getUnconscious());
        log.info("breathingDifficulty : {} " , emergency.getBreathingDifficulty());
        log.info("severeBleeding : {} " , emergency.getSevereBleeding());
        log.info("trappedInVehicle : {} " , emergency.getTrappedInVehicle());
        log.info("occurredAt : {} " , emergency.getOccurredAt());

    }
    @KafkaListener(topics = "ambulance.assigned" , groupId = "hospital-group" , properties = {
            "spring.json.value.default.type=com.jayant.hospitalservice.Kafka.DTO.AmbulanceAssignedEvent"})
    public void consumingEventFromAmbulanceAssignmentService(AmbulanceAssignedEvent ambulance){
        log.info("eventId : {} " , ambulance.getEventId());
        log.info("emergencyId : {} " , ambulance.getEmergencyId());
        log.info("ambulanceId : {} " , ambulance.getAmbulanceId());
        log.info("assignedAt : {} " , ambulance.getAssignedAt());
    }

}
