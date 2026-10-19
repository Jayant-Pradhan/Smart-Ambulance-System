package com.jayant.trackingservice.Kafka.Consumer;


import com.jayant.trackingservice.Kafka.DTO.AmbulanceAssignmentEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AssignmentEventConsumer {

    @KafkaListener(topics = "ambulance.assigned", groupId = "tracking-group",properties = {
            "spring.json.value.default.type=com.jayant.trackingservice.Kafka.DTO.AmbulanceAssignmentEventDTO",
            "spring.json.use.type.headers=false"
    })
    public void consumingEventFromAssignmentService(AmbulanceAssignmentEventDTO assignment) {

        log.info("---------- Ambulance Assignment details: --------------");
        log.info("emergencyId: {}", assignment.getEmergencyId());
        log.info("ambulanceId: {}", assignment.getAmbulanceId());
        log.info("assignedAt: {}", assignment.getAssignedAt());
    }
}
