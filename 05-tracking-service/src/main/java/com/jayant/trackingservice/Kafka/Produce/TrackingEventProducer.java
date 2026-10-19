package com.jayant.trackingservice.Kafka.Produce;

import com.jayant.trackingservice.Kafka.DTO.TrackingEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackingEventProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private static final String TOPIC="tracking.details";

    public void producingEventFromTrackingService(TrackingEventDTO eventDTO){

        kafkaTemplate.send(TOPIC, String.valueOf(eventDTO.getEmergencyId()), eventDTO);
    }
}
