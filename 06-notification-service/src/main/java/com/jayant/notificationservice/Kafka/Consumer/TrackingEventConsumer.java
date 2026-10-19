package com.jayant.notificationservice.Kafka.Consumer;

import com.jayant.notificationservice.Kafka.DTO.TrackingEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrackingEventConsumer {

    private static final String TOPIC = "tracking.details";

    @KafkaListener(
            topics = TOPIC,
            groupId = "notification-group",
            properties = {
                    "spring.json.value.default.type=com.jayant.notificationservice.Kafka.DTO.TrackingEventDTO",
                    "spring.json.use.type.headers=false"
            }
    )
    public void consumeTrackingEvent(TrackingEventDTO event) {

        log.info("========== Tracking Event Received ==========");
        log.info("Emergency ID: {}", event.getEmergencyId());
        log.info("Ambulance ID: {}", event.getAmbulanceId());
        log.info("Latitude: {}", event.getLatitude());
        log.info("Longitude: {}", event.getLongitude());
        log.info("Trip Status: {}", event.getTripStatus());
        log.info("Updated At: {}", event.getUpdatedAt());
    }
}