package com.jayant.notificationservice.Kafka.Consumer;

import com.jayant.notificationservice.Kafka.DTO.AmbulanceAssignmentEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AssignmentEventConsumer {

    private static final String AMBULANCE_ASSIGNED_TOPIC = "ambulance.assigned";

    @KafkaListener(
            topics = AMBULANCE_ASSIGNED_TOPIC,
            groupId = "notification-group",
            properties = {
                    "spring.json.value.default.type=com.jayant.notificationservice.Kafka.DTO.AmbulanceAssignmentEventDTO",
                    "spring.json.use.type.headers=false"
            }
    )
    public void consumeAssignmentEvent(AmbulanceAssignmentEventDTO event) {

        log.info("========== Ambulance Assignment Event Received ==========");
        log.info("Assignment ID: {}", event.getAssignmentId());
        log.info("Emergency ID: {}", event.getEmergencyId());
        log.info("Ambulance ID: {}", event.getAmbulanceId());
        log.info("Distance In KM: {}", event.getDistanceInKm());
        log.info("Assignment Status: {}", event.getAssignmentStatus());
        log.info("Assigned At: {}", event.getAssignedAt());
    }
}