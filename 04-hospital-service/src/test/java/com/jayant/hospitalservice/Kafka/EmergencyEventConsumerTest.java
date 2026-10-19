package com.jayant.hospitalservice.Kafka;

import com.jayant.hospitalservice.Kafka.Consumer.EmergencyEventConsumer;
import com.jayant.hospitalservice.Kafka.DTO.AmbulanceAssignedEvent;
import com.jayant.hospitalservice.Kafka.DTO.EmergencyCreatedEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmergencyEventConsumerTest {

    private final EmergencyEventConsumer emergencyEventConsumer = new EmergencyEventConsumer();

    // 1. consumingEventFromEmergencyService
    @Test
    void consumingEventFromEmergencyService_shouldProcessEventWithoutError() {
        // Arrange
        EmergencyCreatedEvent event = new EmergencyCreatedEvent();
        event.setEventId("event-1");
        event.setEmergencyId(5L);
        event.setPatientCount(2);
        event.setLatitude(19.3149);
        event.setLongitude(84.7941);
        event.setUnconscious(true);
        event.setBreathingDifficulty(false);
        event.setSevereBleeding(false);
        event.setTrappedInVehicle(false);
        event.setOccurredAt(LocalDateTime.now());

        // Act and Assert
        assertDoesNotThrow(() -> emergencyEventConsumer.consumingEventFromEmergencyService(event));
    }

    // 2. consumingEventFromAmbulanceAssignmentService
    @Test
    void consumingEventFromAmbulanceAssignmentService_shouldProcessEventWithoutError() {
        // Arrange
        AmbulanceAssignedEvent event = new AmbulanceAssignedEvent();
        event.setEventId("event-2");
        event.setEmergencyId(5L);
        event.setAmbulanceId(20L);
        event.setAssignedAt(LocalDateTime.now());

        // Act and Assert
        assertDoesNotThrow(() -> emergencyEventConsumer.consumingEventFromAmbulanceAssignmentService(event));
    }
}