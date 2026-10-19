package com.jayant.hospitalservice.Service.Implementation;

import com.jayant.hospitalservice.DTO.HospitalResponseDTO;
import com.jayant.hospitalservice.Entity.Bed;
import com.jayant.hospitalservice.Entity.Department;
import com.jayant.hospitalservice.Entity.Hospital;
import com.jayant.hospitalservice.Enum.BedStatus;
import com.jayant.hospitalservice.Enum.BedType;
import com.jayant.hospitalservice.Exception.InsufficientBedAvailabilityException;
import com.jayant.hospitalservice.Kafka.DTO.EmergencyCreatedEvent;
import com.jayant.hospitalservice.Kafka.DTO.HospitalSelectedEvent;
import com.jayant.hospitalservice.Kafka.Producer.HospitalEventProducer;
import com.jayant.hospitalservice.Mapper.HospitalMapper;
import com.jayant.hospitalservice.Repository.BedRepository;
import com.jayant.hospitalservice.Repository.DepartmentRepository;
import com.jayant.hospitalservice.Repository.HospitalRepository;
import com.jayant.hospitalservice.Service.BedService;
import com.jayant.hospitalservice.Service.HospitalSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalSelectionServiceImpl implements HospitalSelectionService {

    private final HospitalRepository hospitalRepository;
    private final DepartmentRepository departmentRepository;
    private final BedRepository bedRepository;
    private final BedService bedService;

    private final HospitalEventProducer hospitalEventProducer;

    @Override
    public HospitalResponseDTO selectHospital(EmergencyCreatedEvent event) {

        /// condition : 1 { Hospital should be active }
        List<Hospital> activeHospital = hospitalRepository.findByActiveTrue();

        List<Hospital> matchingHospital = new ArrayList<>();

        for(Hospital hospital : activeHospital){
            Long id  = hospital.getId();
            List<Department> departmentList = departmentRepository.findByHospitalIdAndActiveTrue(id);
            for(Department department : departmentList){
                if(department.getName().equalsIgnoreCase("Emergency")){
                    matchingHospital.add(hospital);
                }
            }
        }

        BedType requiredBedType;
        if(Boolean.TRUE.equals(event.getBreathingDifficulty())){
            requiredBedType = BedType.VENTILATOR;
        } else if (Boolean.TRUE.equals(event.getUnconscious())) {
            requiredBedType = BedType.ICU;
        }
        else{
            requiredBedType = BedType.GENERAL;
        }
        List<Hospital> hospitalsWithAvailableBeds = new ArrayList<>();
        for(Hospital hospital : matchingHospital){
            Long id = hospital.getId();
            List<Bed> availableBed = bedRepository.findByHospitalIdAndActiveTrueAndBedTypeAndStatus(hospital.getId() , requiredBedType , BedStatus.AVAILABLE);
            if(!availableBed.isEmpty()){

                hospitalsWithAvailableBeds.add(hospital);
            }
        }

        Hospital selectedHospital = null;
        double minDistance = Double.MAX_VALUE;

        for(Hospital hospital : hospitalsWithAvailableBeds){
            double distance = calculateDistance(event.getLatitude(), event.getLongitude(), hospital.getLatitude(), hospital.getLongitude());
            if (distance < minDistance) {
                minDistance = distance;
                selectedHospital = hospital;
            }
        }
        if (selectedHospital == null) {
            throw new InsufficientBedAvailabilityException(
                    "No suitable hospital found with required bed availability"
            );
        }
        List<Bed> availableBeds = bedRepository.findByHospitalIdAndActiveTrueAndBedTypeAndStatus(selectedHospital.getId(), requiredBedType, BedStatus.AVAILABLE);
        if (availableBeds.isEmpty()) {
            throw new InsufficientBedAvailabilityException(
                    "No available bed found in selected hospital"
            );
        }
        Bed selectedBed = availableBeds.get(0);

        bedService.reserveBed(selectedBed.getId());

        // kafka
        HospitalSelectedEvent eventDTO = new HospitalSelectedEvent();

        eventDTO.setEventId(java.util.UUID.randomUUID().toString());
        eventDTO.setEmergencyId(event.getEmergencyId());
        eventDTO.setHospitalId(selectedHospital.getId());
        eventDTO.setHospitalName(selectedHospital.getName());
        eventDTO.setHospitalAddress(selectedHospital.getAddress());
        eventDTO.setLatitude(selectedHospital.getLatitude());
        eventDTO.setLongitude(selectedHospital.getLongitude());
        eventDTO.setBedId(selectedBed.getId());
        eventDTO.setBedType(selectedBed.getBedType());
        eventDTO.setOccurredAt(java.time.LocalDateTime.now());

        hospitalEventProducer.publishHospitalSelectedEvent(eventDTO);

        return HospitalMapper.toResponseDTO(selectedHospital);
    }

    private double calculateDistance(Double latitude, Double longitude, Double latitude1, Double longitude1) {
        double earthRadius = 6371;

        double latDistance = Math.toRadians(latitude1 - latitude);
        double lonDistance = Math.toRadians(longitude1 - longitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitude))
                * Math.cos(Math.toRadians(latitude1))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }
}
