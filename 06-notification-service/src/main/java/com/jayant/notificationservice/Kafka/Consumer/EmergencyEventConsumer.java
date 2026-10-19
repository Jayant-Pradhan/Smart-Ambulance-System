package com.jayant.notificationservice.Kafka.Consumer;

import com.jayant.notificationservice.Client.EmailProviderClient;
import com.jayant.notificationservice.Kafka.DTO.EmergencyEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmergencyEventConsumer {

    private static final String EMERGENCY_TOPIC = "emergency.details";

    private final EmailProviderClient emailProviderClient;

    @KafkaListener(
            topics = EMERGENCY_TOPIC,
            groupId = "notification-group",
            properties = {
                    "spring.json.value.default.type=com.jayant.notificationservice.Kafka.DTO.EmergencyEventDTO",
                    "spring.json.use.type.headers=false"
            }
    )
    public void consumeEmergencyEvent(EmergencyEventDTO event) {

        log.info("========== Emergency Event Received ==========");
        log.info("Event ID: {}", event.getEventId());
        log.info("Emergency ID: {}", event.getEmergencyId());
        log.info("Emergency Type: {}", event.getEmergencyType());
        log.info("Caller Email: {}", event.getCallerEmail());
        log.info("Patient Count: {}", event.getPatientCount());
        log.info("Latitude: {}", event.getLatitude());
        log.info("Longitude: {}", event.getLongitude());
        log.info("Status: {}", event.getStatus());

        String recipientEmail = event.getCallerEmail();

        if (recipientEmail == null || recipientEmail.isBlank()) {
            log.warn("Caller email is missing for emergency ID: {}",
                    event.getEmergencyId());
            return;
        }

        String subject = "Smart Ambulance - Emergency Registered";

        String message = """
                Your emergency request has been registered successfully.

                Emergency ID: %s
                Emergency Type: %s
                Patient Count: %s
                Status: %s
                """.formatted(
                event.getEmergencyId(),
                event.getEmergencyType(),
                event.getPatientCount(),
                event.getStatus()
        );

        emailProviderClient.sendEmail(
                recipientEmail,
                subject,
                message
        );

        log.info("Emergency notification email sent to: {}", recipientEmail);
    }
}