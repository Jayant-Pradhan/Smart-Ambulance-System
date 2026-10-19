package com.jayant.ambulanceservice.Service;

import com.jayant.ambulanceservice.DTO.Request.RegisterAmbulanceRequest;
import com.jayant.ambulanceservice.DTO.Response.AmbulanceResponse;
import com.jayant.ambulanceservice.Entity.Ambulance;
import com.jayant.ambulanceservice.Enum.AmbulanceStatus;
import com.jayant.ambulanceservice.Exception.AmbulanceNotFoundException;
import com.jayant.ambulanceservice.Exception.DuplicateAmbulanceException;
import com.jayant.ambulanceservice.Kafka.AmbulanceEventProducer;
import com.jayant.ambulanceservice.Kafka.Event.AmbulanceRegisteredEvent;
import com.jayant.ambulanceservice.Kafka.Event.AmbulanceStatusChangedEvent;
import com.jayant.ambulanceservice.Mapper.AmbulanceMapper;
import com.jayant.ambulanceservice.Repository.AmbulanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AmbulanceServiceImpl implements AmbulanceService {

    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceMapper ambulanceMapper;
    private final AmbulanceEventProducer ambulanceEventProducer;

    @Override
    public AmbulanceResponse registrationAmbulance(RegisterAmbulanceRequest ambulanceRequest) {
       if(ambulanceRepository.existsByRegistrationNumber(ambulanceRequest.getRegistrationNumber())){
           throw new DuplicateAmbulanceException("Ambulance registration number already exists");
       }
       Ambulance ambulance = ambulanceMapper.toEntity(ambulanceRequest);
       Ambulance savedAmbulance = ambulanceRepository.save(ambulance);

       // kafka
        ambulanceEventProducer.publishAmbulanceRegistered(new AmbulanceRegisteredEvent(
                savedAmbulance.getId(),savedAmbulance.getAmbulanceType(),savedAmbulance.getPatientCapacity(),
                savedAmbulance.getHasOxygen() , savedAmbulance.getHasVentilator() , savedAmbulance.getHasCardiacMonitor(),
                savedAmbulance.getLatitude() , savedAmbulance.getLongitude() , savedAmbulance.getStatus(), LocalDateTime.now()

        ));

       return ambulanceMapper.toResponse( savedAmbulance);
    }

    @Override
    public AmbulanceResponse getAmbulanceById(Long id) {

        Optional<Ambulance> isAvail = ambulanceRepository.findById(id);

        if(isAvail.isPresent()){
            Ambulance ambulance = isAvail.get();
            return ambulanceMapper.toResponse(ambulance);
        }

        throw new AmbulanceNotFoundException("ambulance is not found using this ambulance id : " + id );
    }

    @Override
    public List<AmbulanceResponse> getAllAmbulances() {

        List<AmbulanceResponse> responses = new ArrayList<>();

        List<Ambulance> list = ambulanceRepository.findAll();

        for(Ambulance ambulance : list){
            responses.add( ambulanceMapper.toResponse(ambulance));
        }

        return responses;

    }

    @Override
    public AmbulanceResponse updateAmbulanceStatus(Long id, AmbulanceStatus status) {
        Optional<Ambulance> isAvail = ambulanceRepository.findById(id);

        if(isAvail.isPresent()){
            Ambulance ambulance = isAvail.get();
            // Store previous status before updating
            AmbulanceStatus previousStatus = ambulance.getStatus();
            ambulance.setStatus(status);
            Ambulance updatedAmbulance = ambulanceRepository.save(ambulance);


            // Kafka event
            ambulanceEventProducer.publishAmbulanceStatusChanged(
                    new AmbulanceStatusChangedEvent(
                            updatedAmbulance.getId(),
                            previousStatus,
                            updatedAmbulance.getStatus(),
                            LocalDateTime.now()
                    )
            );

            return ambulanceMapper.toResponse(updatedAmbulance);

        }
        throw new AmbulanceNotFoundException("ambulance is not found using this ambulance id :" + id);
    }

    @Override
    public AmbulanceResponse updateAmbulanceLocation(Long id, Double latitude, Double longitude) {
        Optional<Ambulance> isAvail = ambulanceRepository.findById(id);
        if(isAvail.isPresent()){
            Ambulance ambulance = isAvail.get();
            ambulance.setLatitude(latitude);
            ambulance.setLongitude(longitude);
            Ambulance updatedambulance = ambulanceRepository.save(ambulance);
            return ambulanceMapper.toResponse(updatedambulance);
        }
        throw new AmbulanceNotFoundException("ambulance is not found using this ambulance id : " + id);

    }
}
