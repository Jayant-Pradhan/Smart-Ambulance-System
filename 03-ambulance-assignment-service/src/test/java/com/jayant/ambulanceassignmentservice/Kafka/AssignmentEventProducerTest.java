package com.jayant.ambulanceassignmentservice.Kafka;

import com.jayant.ambulanceassignmentservice.Enum.AssignmentStatus;
import com.jayant.ambulanceassignmentservice.Kafka.Consumer.AssignmentEventProducer;
import com.jayant.ambulanceassignmentservice.Kafka.Event.AmbulanceAssignedEventData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssignmentEventProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private AssignmentEventProducer assignmentEventProducer;

    private AmbulanceAssignedEventData buildEvent() {
        return new AmbulanceAssignedEventData(
                5L,
                10L,
                1L,
                2.5,
                AssignmentStatus.ASSIGNED,
                LocalDateTime.now()
        );
    }

    @Test
    void producingAmbulanceAssignment_shouldSendEventToCorrectTopicWithAssignmentIdAsKey() {

        AmbulanceAssignedEventData event = buildEvent();

        assignmentEventProducer.producingAmbulanceAssignment(event);

        verify(kafkaTemplate).send("ambulance.assigned", "5", event);
        verifyNoMoreInteractions(kafkaTemplate);
    }

    @Test
    void producingAmbulanceAssignment_shouldPassSameEventObject() {

        AmbulanceAssignedEventData event = buildEvent();

        ArgumentCaptor<Object> payloadCaptor = ArgumentCaptor.forClass(Object.class);

        assignmentEventProducer.producingAmbulanceAssignment(event);

        verify(kafkaTemplate).send(anyString(), anyString(), payloadCaptor.capture());
        assertSame(event, payloadCaptor.getValue());
    }

    @Test
    void producingAmbulanceAssignment_whenKafkaFails_shouldPropagateException() {

        AmbulanceAssignedEventData event = buildEvent();

        when(kafkaTemplate.send(anyString(), anyString(), any()))
                .thenThrow(new RuntimeException("Kafka down"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> assignmentEventProducer.producingAmbulanceAssignment(event));

        assertEquals("Kafka down", ex.getMessage());
    }
}