package com.jayant.ambulanceassignmentservice.Service;

import com.jayant.ambulanceassignmentservice.Entity.Ambulance;
import com.jayant.ambulanceassignmentservice.Entity.AmbulanceAssignment;
import com.jayant.ambulanceassignmentservice.Entity.Emergency;
import com.jayant.ambulanceassignmentservice.Enum.AssignmentStatus;
import com.jayant.ambulanceassignmentservice.Kafka.Consumer.AssignmentEventProducer;
import com.jayant.ambulanceassignmentservice.Kafka.Event.AmbulanceAssignedEventData;
import com.jayant.ambulanceassignmentservice.Repository.AmbulanceRepository;
import com.jayant.ambulanceassignmentservice.Repository.AssignmentRepository;
import com.jayant.ambulanceassignmentservice.Repository.EmergencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final EmergencyRepository emergencyRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final AssignmentEventProducer assignmentEventProducer;

    @Override
    public AmbulanceAssignment assignAmbulance(Long emergencyId) {

        Optional<AmbulanceAssignment> isAvail = assignmentRepository.findByEmergencyId(emergencyId);

        if(isAvail.isPresent()){
            AmbulanceAssignment ambulanceAssignment = isAvail.get();
            return ambulanceAssignment;
        }
        else{
            Optional<Emergency> emergency = emergencyRepository.findById(emergencyId);
            if(emergency.isEmpty()){
                throw new RuntimeException("emergency id is not found in emergency DB");
            }
            else{
                Emergency emergency1 = emergency.get();
                List<Ambulance> availableAmbulances = ambulanceRepository.findByStatus("AVAILABLE");

                List<Ambulance> ambulanceList = new ArrayList<>();
                for(Ambulance ambulance : availableAmbulances){
                    if(ambulance.getPatientCapacity() >= emergency1.getPatientCount()){
                        ambulanceList.add(ambulance);
                    }
                }
                if (ambulanceList.isEmpty()) {
                    throw new RuntimeException("No suitable ambulance available for patient capacity");
                }
                double minDistance = Double.MAX_VALUE;
                Ambulance selectedAmbulance = null;

                for (Ambulance ambulance : ambulanceList) {
                    double distance = calculateDistance(
                            emergency1.getLatitude(),
                            emergency1.getLongitude(),
                            ambulance.getLatitude(),
                            ambulance.getLongitude()
                    );

                    if (distance < minDistance) {
                        minDistance = distance;
                        selectedAmbulance = ambulance;
                    }
                }

                if (selectedAmbulance == null) {
                    throw new RuntimeException("No suitable ambulance found");
                }

                AmbulanceAssignment assignment = new AmbulanceAssignment();

                assignment.setEmergencyId(emergency1.getEmergencyId());
                assignment.setAmbulanceId(selectedAmbulance.getAmbulanceId());
                assignment.setDistanceInKm(minDistance);
                assignment.setAssignmentStatus(AssignmentStatus.ASSIGNED);
                assignment.setAssignedAt(LocalDateTime.now());

                AmbulanceAssignment savedAssignment = assignmentRepository.save(assignment);


                selectedAmbulance.setStatus("BUSY");
                ambulanceRepository.save(selectedAmbulance);


                assignmentEventProducer.producingAmbulanceAssignment(new AmbulanceAssignedEventData(
                        savedAssignment.getAssignmentId() , savedAssignment.getEmergencyId() ,
                        savedAssignment.getAmbulanceId() , savedAssignment.getDistanceInKm() ,
                        savedAssignment.getAssignmentStatus() ,
                        savedAssignment.getAssignedAt()
                ));

                return savedAssignment;




            }

        }


    }
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        final double EARTH_RADIUS_KM = 6371.0;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    @Override
    public AmbulanceAssignment findAssignmentByEmergencyId(Long emergencyId) {

        Optional<AmbulanceAssignment> isAvail = assignmentRepository.findByEmergencyId(emergencyId);

        if(isAvail.isPresent()){
            AmbulanceAssignment ambulanceAssignment = isAvail.get();
            return ambulanceAssignment;
        }
        throw new RuntimeException("emergency id is not found with this id : " + emergencyId);
    }

    @Override
    public AmbulanceAssignment findAssignmentById(Long assignmentId) {
        Optional<AmbulanceAssignment> isAvail = assignmentRepository.findById(assignmentId);
        if(isAvail.isPresent()){
            AmbulanceAssignment ambulanceAssignment = isAvail.get();
            return ambulanceAssignment;
        }
        throw new RuntimeException("assignment id not found using this id : " + assignmentId);
    }

    @Override
    public AmbulanceAssignment updateAssignmentStatus(Long assignmentId, AssignmentStatus status) {
        Optional<AmbulanceAssignment> isAvail = assignmentRepository.findById(assignmentId);

        if (isAvail.isPresent()) {
            AmbulanceAssignment ambulanceAssignment = isAvail.get();
            ambulanceAssignment.setAssignmentStatus(status);

            if(status == AssignmentStatus.COMPLETED){
                ambulanceAssignment.setCompletedAt(LocalDateTime.now());
            }
            AmbulanceAssignment updatedAssignment  = assignmentRepository.save(ambulanceAssignment);
            return updatedAssignment ;
        }
        throw new RuntimeException("assignment id is not found with this " + assignmentId);
    }

    @Override
    public List<AmbulanceAssignment> getAllAssignments() {

        return assignmentRepository.findAll();
    }

    @Override
    public List<AmbulanceAssignment> getAssignmentsByStatus(AssignmentStatus status) {
        return assignmentRepository.findAllByAssignmentStatus(status);
    }

}
