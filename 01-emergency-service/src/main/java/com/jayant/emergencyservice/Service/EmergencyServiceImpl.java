package com.jayant.emergencyservice.Service;

import com.jayant.emergencyservice.DTO.Request.CreateEmergencyRequest;
import com.jayant.emergencyservice.DTO.Response.EmergencyResponse;
import com.jayant.emergencyservice.Entity.Emergency;
import com.jayant.emergencyservice.Enum.EmergencyStatus;
import com.jayant.emergencyservice.Kafka.EmergencyEvent;
import com.jayant.emergencyservice.Kafka.EmergencyEventProducer;
import com.jayant.emergencyservice.Mapper.EmergencyMapper;
import com.jayant.emergencyservice.Repository.EmergencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmergencyServiceImpl  implements  EmergencyService{

    private final EmergencyRepository emergencyRepository;
    private final EmergencyMapper emergencyMapper;
    private final EmergencyEventProducer emergencyEventProducer;


    @Override
    public EmergencyResponse createEmergency(CreateEmergencyRequest request) {


//        return emergencyMapper.toResponse(emergencyRepository.save(emergencyMapper.toEntity(request)));
        Emergency emergency = emergencyRepository.save(emergencyMapper.toEntity(request));

        EmergencyEvent event = new EmergencyEvent();

        event.setEventId(UUID.randomUUID().toString());
        event.setEmergencyId(emergency.getId());
        event.setEmergencyType(emergency.getEmergencyType());
        event.setCallerEmail(emergency.getCallerEmail());
        event.setPatientCount(emergency.getPatientCount());
        event.setLatitude(emergency.getLatitude());
        event.setLongitude(emergency.getLongitude());

        event.setUnconscious(emergency.getUnconscious());
        event.setBreathingDifficulty(emergency.getBreathingDifficulty());
        event.setSevereBleeding(emergency.getSevereBleeding());
        event.setTrappedInVehicle(emergency.getTrappedInVehicle());
        event.setStatus(emergency.getStatus());

        event.setOccurredAt(java.time.LocalDateTime.now());

        emergencyEventProducer.sendEmergencyToEvent(event);

        return emergencyMapper.toResponse(emergency);
    }

    @Override
    public EmergencyResponse getEmergencyDetailsUsingId(Long id) {
        Optional<Emergency>  isAvail = emergencyRepository.findById(id);

        if(isAvail.isPresent()){
            Emergency present = isAvail.get();
            return emergencyMapper.toResponse(present);
        }
        throw new RuntimeException("Emergency not found with id : " + id);
    }

    @Override
    public List<EmergencyResponse> getAllEmergencyDetails() {
        List<Emergency> listOfEmergence = emergencyRepository.findAll();

        List<EmergencyResponse> list = new ArrayList<>();  // empty array list

        for(Emergency emergency : listOfEmergence){
            list.add(emergencyMapper.toResponse(emergency));
        }
        return list;

    }

    @Override
    public EmergencyResponse cancelEmergency(Long id) {

        Optional<Emergency> isAvail = emergencyRepository.findById(id);

        if(isAvail.isPresent()){

            Emergency emergency = isAvail.get();
            emergency.setStatus(EmergencyStatus.CANCELLED);
            Emergency savedEmergency = emergencyRepository.save(emergency);

            EmergencyEvent event = new EmergencyEvent();
            event.setEventId(UUID.randomUUID().toString());
            event.setEmergencyId(savedEmergency.getId());
            event.setStatus(savedEmergency.getStatus());
            event.setEmergencyType(savedEmergency.getEmergencyType());
            event.setPatientCount(savedEmergency.getPatientCount());
            event.setLatitude(savedEmergency.getLatitude());
            event.setLongitude(savedEmergency.getLongitude());
            event.setUnconscious(savedEmergency.getUnconscious());
            event.setBreathingDifficulty(savedEmergency.getBreathingDifficulty());
            event.setSevereBleeding(savedEmergency.getSevereBleeding());
            event.setTrappedInVehicle(savedEmergency.getTrappedInVehicle());
            event.setOccurredAt(LocalDateTime.now());

            emergencyEventProducer.sendEmergencyToEvent(event);

            return emergencyMapper.toResponse(savedEmergency);

        }

        throw new RuntimeException("emergency not found with this id : " + id);

    }

    @Override
    public EmergencyResponse getEmergencyStatus(Long id) {

        Optional<Emergency> isAvail = emergencyRepository.findById(id);

        if(isAvail.isPresent()){
            Emergency emergency = isAvail.get();
            return emergencyMapper.toResponse(emergency);

        }
        throw new RuntimeException("Emergency not found with this is :  " + id);

    }
}
