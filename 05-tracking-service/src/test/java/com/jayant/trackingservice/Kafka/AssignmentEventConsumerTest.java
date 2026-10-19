package com.jayant.trackingservice.Kafka;

import com.jayant.trackingservice.Kafka.Consumer.AssignmentEventConsumer;
import com.jayant.trackingservice.Kafka.DTO.AmbulanceAssignmentEventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AssignmentEventConsumerTest {

    private AssignmentEventConsumer assignmentEventConsumer;

    @BeforeEach
    void setUp() {
        assignmentEventConsumer = new AssignmentEventConsumer();
    }

    @Test
    public void consumingEventFromAssignmentService_validEvent_doesNotThrow() {
        AmbulanceAssignmentEventDTO assignment = new AmbulanceAssignmentEventDTO(
                4L,
                9L,
                LocalDateTime.now()
        );

        assertDoesNotThrow(() ->
                assignmentEventConsumer.consumingEventFromAssignmentService(assignment));
    }

    @Test
    public void consumingEventFromAssignmentService_nullFields_doesNotThrow() {
        AmbulanceAssignmentEventDTO assignment = new AmbulanceAssignmentEventDTO(null, null, null);

        assertDoesNotThrow(() ->
                assignmentEventConsumer.consumingEventFromAssignmentService(assignment));
    }

    @Test
    public void consumingEventFromAssignmentService_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                assignmentEventConsumer.consumingEventFromAssignmentService(null));
    }
}