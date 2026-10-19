package com.jayant.trackingservice.Kafka;

import com.jayant.trackingservice.Kafka.Consumer.EmergencyEventConsumer;
import com.jayant.trackingservice.Kafka.DTO.EmergencyEventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmergencyEventConsumerTest {

    private EmergencyEventConsumer emergencyEventConsumer;

    @BeforeEach
    void setUp() {
        emergencyEventConsumer = new EmergencyEventConsumer();
    }

    @Test
    public void consumingEventFromEmergencyService_validEvent_doesNotThrow() {
        EmergencyEventDTO emergency = new EmergencyEventDTO(
                4L,
                "ACCIDENT",
                20.2961,
                85.8245,
                "CREATED"
        );

        assertDoesNotThrow(() ->
                emergencyEventConsumer.consumingEventFromEmergencyService(emergency));
    }

    @Test
    public void consumingEventFromEmergencyService_nullFields_doesNotThrow() {
        EmergencyEventDTO emergency = new EmergencyEventDTO(null, null, null, null, null);

        assertDoesNotThrow(() ->
                emergencyEventConsumer.consumingEventFromEmergencyService(emergency));
    }

    @Test
    public void consumingEventFromEmergencyService_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                emergencyEventConsumer.consumingEventFromEmergencyService(null));
    }
}