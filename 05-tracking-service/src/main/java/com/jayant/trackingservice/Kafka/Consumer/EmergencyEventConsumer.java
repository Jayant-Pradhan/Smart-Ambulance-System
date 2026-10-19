package com.jayant.trackingservice.Kafka.Consumer;


import com.jayant.trackingservice.Kafka.DTO.EmergencyEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmergencyEventConsumer {

    @KafkaListener(topics = "emergency.details" , groupId = "tracking-group" ,properties = {
            "spring.json.value.default.type=com.jayant.trackingservice.Kafka.DTO.EmergencyEventDTO",
            "spring.json.use.type.headers=false"
    })
    public void consumingEventFromEmergencyService(EmergencyEventDTO emergency){
        log.info("----------    Emergency details :  -------------- ");
        log.info("emergencyId : {} " , emergency.getEmergencyId());
        log.info("emergencyType : {} " , emergency.getEmergencyType());
        log.info("latitude : {} " , emergency.getLatitude());
        log.info("longitude : {} " , emergency.getLongitude());
        log.info("status : {}  " , emergency.getStatus());

    }


}
