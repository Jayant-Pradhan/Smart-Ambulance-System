package com.jayant.emergencyservice.Kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmergencyEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    private static final String EMERGENCY_TOPIC="emergency.details";

    public void sendEmergencyToEvent(EmergencyEvent event){

        kafkaTemplate.send(EMERGENCY_TOPIC, String.valueOf(event.getEmergencyId()),event);
    }
}
