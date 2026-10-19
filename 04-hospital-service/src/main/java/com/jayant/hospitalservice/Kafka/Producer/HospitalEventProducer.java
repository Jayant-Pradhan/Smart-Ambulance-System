package com.jayant.hospitalservice.Kafka.Producer;

import com.jayant.hospitalservice.Kafka.DTO.HospitalSelectedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalEventProducer {

    private final KafkaTemplate<String, HospitalSelectedEvent> kafkaTemplate;

    private static final String TOPIC = "hospital.selected";

    public void publishHospitalSelectedEvent(HospitalSelectedEvent event) {
        kafkaTemplate.send(TOPIC, event.getEmergencyId().toString(), event);
    }
}
