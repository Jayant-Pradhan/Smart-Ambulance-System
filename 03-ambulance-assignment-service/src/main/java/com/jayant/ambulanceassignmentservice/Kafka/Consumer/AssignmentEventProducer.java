package com.jayant.ambulanceassignmentservice.Kafka.Consumer;

import com.jayant.ambulanceassignmentservice.Kafka.Event.AmbulanceAssignedEventData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentEventProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    private static final String AMBULANCE_ASSIGNED_TOPIC="ambulance.assigned";

    public void producingAmbulanceAssignment(AmbulanceAssignedEventData eventData){
        kafkaTemplate.send(AMBULANCE_ASSIGNED_TOPIC , String.valueOf(eventData.getAssignmentId()), eventData);
    }
}
