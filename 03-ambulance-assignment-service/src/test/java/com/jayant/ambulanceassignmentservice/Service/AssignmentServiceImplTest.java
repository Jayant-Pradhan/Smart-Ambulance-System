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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssignmentServiceImplTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private EmergencyRepository emergencyRepository;

    @Mock
    private AmbulanceRepository ambulanceRepository;

    @Mock
    private AssignmentEventProducer assignmentEventProducer;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    // ---------- helper methods ----------

    private Emergency buildEmergency(Long id, int patientCount, double lat, double lon) {
        Emergency emergency = new Emergency();
        emergency.setEmergencyId(id);
        emergency.setPatientCount(patientCount);
        emergency.setLatitude(lat);
        emergency.setLongitude(lon);
        return emergency;
    }

    private Ambulance buildAmbulance(Long id, int capacity, double lat, double lon) {
        Ambulance ambulance = new Ambulance();
        ambulance.setAmbulanceId(id);
        ambulance.setPatientCapacity(capacity);
        ambulance.setLatitude(lat);
        ambulance.setLongitude(lon);
        ambulance.setStatus("AVAILABLE");
        return ambulance;
    }

    // ---------- assignAmbulance ----------

    @Test
    void assignAmbulance_whenAssignmentAlreadyExists_shouldReturnExisting() {

        AmbulanceAssignment existing = new AmbulanceAssignment();

        when(assignmentRepository.findByEmergencyId(10L))
                .thenReturn(Optional.of(existing));

        AmbulanceAssignment result = assignmentService.assignAmbulance(10L);

        assertSame(existing, result);

        verifyNoInteractions(emergencyRepository, ambulanceRepository, assignmentEventProducer);
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    void assignAmbulance_whenEmergencyNotFound_shouldThrowException() {

        when(assignmentRepository.findByEmergencyId(10L))
                .thenReturn(Optional.empty());
        when(emergencyRepository.findById(10L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> assignmentService.assignAmbulance(10L));

        assertEquals("emergency id is not found in emergency DB", ex.getMessage());

        verifyNoInteractions(ambulanceRepository, assignmentEventProducer);
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    void assignAmbulance_whenNoAmbulanceAvailable_shouldThrowException() {

        Emergency emergency = buildEmergency(10L, 2, 20.0, 85.0);

        when(assignmentRepository.findByEmergencyId(10L))
                .thenReturn(Optional.empty());
        when(emergencyRepository.findById(10L))
                .thenReturn(Optional.of(emergency));
        when(ambulanceRepository.findByStatus("AVAILABLE"))
                .thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> assignmentService.assignAmbulance(10L));

        assertEquals("No suitable ambulance available for patient capacity", ex.getMessage());

        verify(assignmentRepository, never()).save(any());
        verifyNoInteractions(assignmentEventProducer);
    }

    @Test
    void assignAmbulance_whenCapacityTooSmall_shouldThrowException() {

        Emergency emergency = buildEmergency(10L, 5, 20.0, 85.0);
        Ambulance smallAmbulance = buildAmbulance(1L, 2, 20.01, 85.01);

        when(assignmentRepository.findByEmergencyId(10L))
                .thenReturn(Optional.empty());
        when(emergencyRepository.findById(10L))
                .thenReturn(Optional.of(emergency));
        when(ambulanceRepository.findByStatus("AVAILABLE"))
                .thenReturn(List.of(smallAmbulance));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> assignmentService.assignAmbulance(10L));

        assertEquals("No suitable ambulance available for patient capacity", ex.getMessage());

        verify(assignmentRepository, never()).save(any());
        verify(ambulanceRepository, never()).save(any());
        verifyNoInteractions(assignmentEventProducer);
    }

    @Test
    void assignAmbulance_shouldSelectNearestAmbulanceAndPublishEvent() {

        Emergency emergency = buildEmergency(10L, 2, 20.0, 85.0);

        Ambulance nearAmbulance = buildAmbulance(1L, 4, 20.01, 85.01);
        Ambulance farAmbulance = buildAmbulance(2L, 4, 21.0, 86.0);
        Ambulance smallAmbulance = buildAmbulance(3L, 1, 20.0, 85.0); // nearest but capacity too small

        when(assignmentRepository.findByEmergencyId(10L))
                .thenReturn(Optional.empty());
        when(emergencyRepository.findById(10L))
                .thenReturn(Optional.of(emergency));
        when(ambulanceRepository.findByStatus("AVAILABLE"))
                .thenReturn(List.of(farAmbulance, smallAmbulance, nearAmbulance));
        when(assignmentRepository.save(any(AmbulanceAssignment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AmbulanceAssignment result = assignmentService.assignAmbulance(10L);

        // assignment checks
        assertNotNull(result);
        assertEquals(10L, result.getEmergencyId());
        assertEquals(1L, result.getAmbulanceId());
        assertEquals(AssignmentStatus.ASSIGNED, result.getAssignmentStatus());
        assertNotNull(result.getAssignedAt());
        assertTrue(result.getDistanceInKm() > 0);

        // assignment saved
        ArgumentCaptor<AmbulanceAssignment> assignmentCaptor =
                ArgumentCaptor.forClass(AmbulanceAssignment.class);
        verify(assignmentRepository).save(assignmentCaptor.capture());
        assertEquals(1L, assignmentCaptor.getValue().getAmbulanceId());

        // ambulance marked BUSY and saved
        ArgumentCaptor<Ambulance> ambulanceCaptor = ArgumentCaptor.forClass(Ambulance.class);
        verify(ambulanceRepository).save(ambulanceCaptor.capture());
        assertEquals("BUSY", ambulanceCaptor.getValue().getStatus());
        assertEquals(1L, ambulanceCaptor.getValue().getAmbulanceId());

        // kafka event published
        ArgumentCaptor<AmbulanceAssignedEventData> eventCaptor =
                ArgumentCaptor.forClass(AmbulanceAssignedEventData.class);
        verify(assignmentEventProducer).producingAmbulanceAssignment(eventCaptor.capture());
        assertNotNull(eventCaptor.getValue());
    }

    // ---------- findAssignmentByEmergencyId ----------

    @Test
    void findAssignmentByEmergencyId_whenFound_shouldReturnAssignment() {

        AmbulanceAssignment assignment = new AmbulanceAssignment();

        when(assignmentRepository.findByEmergencyId(10L))
                .thenReturn(Optional.of(assignment));

        AmbulanceAssignment result = assignmentService.findAssignmentByEmergencyId(10L);

        assertSame(assignment, result);
        verify(assignmentRepository).findByEmergencyId(10L);
    }

    @Test
    void findAssignmentByEmergencyId_whenNotFound_shouldThrowException() {

        when(assignmentRepository.findByEmergencyId(10L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> assignmentService.findAssignmentByEmergencyId(10L));

        assertEquals("emergency id is not found with this id : 10", ex.getMessage());
    }

    // ---------- findAssignmentById ----------

    @Test
    void findAssignmentById_whenFound_shouldReturnAssignment() {

        AmbulanceAssignment assignment = new AmbulanceAssignment();

        when(assignmentRepository.findById(1L))
                .thenReturn(Optional.of(assignment));

        AmbulanceAssignment result = assignmentService.findAssignmentById(1L);

        assertSame(assignment, result);
        verify(assignmentRepository).findById(1L);
    }

    @Test
    void findAssignmentById_whenNotFound_shouldThrowException() {

        when(assignmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> assignmentService.findAssignmentById(1L));

        assertEquals("assignment id not found using this id : 1", ex.getMessage());
    }

    // ---------- updateAssignmentStatus ----------

    @Test
    void updateAssignmentStatus_whenCompleted_shouldSetCompletedAt() {

        AmbulanceAssignment assignment = new AmbulanceAssignment();

        when(assignmentRepository.findById(1L))
                .thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(any(AmbulanceAssignment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AmbulanceAssignment result =
                assignmentService.updateAssignmentStatus(1L, AssignmentStatus.COMPLETED);

        assertEquals(AssignmentStatus.COMPLETED, result.getAssignmentStatus());
        assertNotNull(result.getCompletedAt());
        verify(assignmentRepository).save(assignment);
    }

    @Test
    void updateAssignmentStatus_whenNotCompleted_shouldNotSetCompletedAt() {

        AmbulanceAssignment assignment = new AmbulanceAssignment();

        when(assignmentRepository.findById(1L))
                .thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(any(AmbulanceAssignment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AmbulanceAssignment result =
                assignmentService.updateAssignmentStatus(1L, AssignmentStatus.ACCEPTED);

        assertEquals(AssignmentStatus.ACCEPTED, result.getAssignmentStatus());
        assertNull(result.getCompletedAt());
        verify(assignmentRepository).save(assignment);
    }

    @Test
    void updateAssignmentStatus_whenNotFound_shouldThrowException() {

        when(assignmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> assignmentService.updateAssignmentStatus(1L, AssignmentStatus.ACCEPTED));

        assertEquals("assignment id is not found with this 1", ex.getMessage());
        verify(assignmentRepository, never()).save(any());
    }

    // ---------- getAllAssignments ----------

    @Test
    void getAllAssignments_shouldReturnList() {

        List<AmbulanceAssignment> assignments =
                List.of(new AmbulanceAssignment(), new AmbulanceAssignment());

        when(assignmentRepository.findAll())
                .thenReturn(assignments);

        List<AmbulanceAssignment> result = assignmentService.getAllAssignments();

        assertEquals(2, result.size());
        verify(assignmentRepository).findAll();
    }

    // ---------- getAssignmentsByStatus ----------

    @Test
    void getAssignmentsByStatus_shouldReturnList() {

        List<AmbulanceAssignment> assignments = List.of(new AmbulanceAssignment());

        when(assignmentRepository.findAllByAssignmentStatus(AssignmentStatus.ACCEPTED))
                .thenReturn(assignments);

        List<AmbulanceAssignment> result =
                assignmentService.getAssignmentsByStatus(AssignmentStatus.ACCEPTED);

        assertEquals(1, result.size());
        verify(assignmentRepository).findAllByAssignmentStatus(AssignmentStatus.ACCEPTED);
    }
}