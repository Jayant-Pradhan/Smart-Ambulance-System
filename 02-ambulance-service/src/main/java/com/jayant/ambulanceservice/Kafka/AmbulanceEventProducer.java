package com.jayant.ambulanceservice.Kafka;

import com.jayant.ambulanceservice.Kafka.Event.AmbulanceRegisteredEvent;
import com.jayant.ambulanceservice.Kafka.Event.AmbulanceStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AmbulanceEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;
    private static final String AMBULANCE_REGISTERED_TOPIC = "ambulance.register";
    private static final String AMBULANCE_STATUS_CHANGED = "ambulance.status.changed";

    public void publishAmbulanceRegistered(AmbulanceRegisteredEvent event) {

        kafkaTemplate.send(AMBULANCE_REGISTERED_TOPIC,String.valueOf(event.getAmbulanceId()), event);
    }
    public void publishAmbulanceStatusChanged(AmbulanceStatusChangedEvent event) {

        kafkaTemplate.send(AMBULANCE_STATUS_CHANGED, String.valueOf(event.getAmbulanceId()), event);
    }


}
