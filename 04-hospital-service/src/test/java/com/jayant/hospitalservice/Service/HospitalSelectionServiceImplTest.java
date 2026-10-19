package com.jayant.hospitalservice.Service;

import com.jayant.hospitalservice.DTO.HospitalResponseDTO;
import com.jayant.hospitalservice.Entity.Bed;
import com.jayant.hospitalservice.Entity.Department;
import com.jayant.hospitalservice.Entity.Hospital;
import com.jayant.hospitalservice.Enum.BedStatus;
import com.jayant.hospitalservice.Enum.BedType;
import com.jayant.hospitalservice.Kafka.DTO.EmergencyCreatedEvent;
import com.jayant.hospitalservice.Kafka.DTO.HospitalSelectedEvent;
import com.jayant.hospitalservice.Kafka.Producer.HospitalEventProducer;
import com.jayant.hospitalservice.Repository.BedRepository;
import com.jayant.hospitalservice.Repository.DepartmentRepository;
import com.jayant.hospitalservice.Repository.HospitalRepository;
import com.jayant.hospitalservice.Service.Implementation.HospitalSelectionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HospitalSelectionServiceImplTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private BedRepository bedRepository;

    @Mock
    private BedService bedService;

    @Mock
    private HospitalEventProducer hospitalEventProducer;

    @InjectMocks
    private HospitalSelectionServiceImpl hospitalSelectionService;

    // selectHospital
    @Test
    void selectHospital_shouldSelectNearestHospitalAndReserveBed() {
        // Arrange
        // Hospital 1: Berhampur ke paas (nearest)
        Hospital nearHospital = new Hospital();
        nearHospital.setId(1L);
        nearHospital.setName("City Care Hospital");
        nearHospital.setAddress("Main Road, Berhampur");
        nearHospital.setLatitude(19.3200);
        nearHospital.setLongitude(84.8000);

        // Hospital 2: Bhubaneswar (door)
        Hospital farHospital = new Hospital();
        farHospital.setId(2L);
        farHospital.setName("Capital Hospital");
        farHospital.setAddress("Station Road, Bhubaneswar");
        farHospital.setLatitude(20.2961);
        farHospital.setLongitude(85.8245);

        List<Hospital> activeHospitals = new ArrayList<>();
        activeHospitals.add(nearHospital);
        activeHospitals.add(farHospital);
        when(hospitalRepository.findByActiveTrue()).thenReturn(activeHospitals);

        // Dono hospitals mein Emergency department hai
        Department emergencyDepartmentOne = new Department();
        emergencyDepartmentOne.setName("Emergency");
        List<Department> departmentsOne = new ArrayList<>();
        departmentsOne.add(emergencyDepartmentOne);
        when(departmentRepository.findByHospitalIdAndActiveTrue(1L)).thenReturn(departmentsOne);

        Department emergencyDepartmentTwo = new Department();
        emergencyDepartmentTwo.setName("Emergency");
        List<Department> departmentsTwo = new ArrayList<>();
        departmentsTwo.add(emergencyDepartmentTwo);
        when(departmentRepository.findByHospitalIdAndActiveTrue(2L)).thenReturn(departmentsTwo);

        // Dono hospitals mein GENERAL bed available hai
        Bed bedOfNearHospital = new Bed();
        bedOfNearHospital.setId(100L);
        bedOfNearHospital.setBedType(BedType.GENERAL);
        List<Bed> bedsOfNearHospital = new ArrayList<>();
        bedsOfNearHospital.add(bedOfNearHospital);
        when(bedRepository.findByHospitalIdAndActiveTrueAndBedTypeAndStatus(
                1L, BedType.GENERAL, BedStatus.AVAILABLE)).thenReturn(bedsOfNearHospital);

        Bed bedOfFarHospital = new Bed();
        bedOfFarHospital.setId(200L);
        bedOfFarHospital.setBedType(BedType.GENERAL);
        List<Bed> bedsOfFarHospital = new ArrayList<>();
        bedsOfFarHospital.add(bedOfFarHospital);
        when(bedRepository.findByHospitalIdAndActiveTrueAndBedTypeAndStatus(
                2L, BedType.GENERAL, BedStatus.AVAILABLE)).thenReturn(bedsOfFarHospital);

        // Emergency Berhampur mein hai, koi serious condition nahi (GENERAL bed chahiye)
        EmergencyCreatedEvent event = new EmergencyCreatedEvent();
        event.setLatitude(19.3149);
        event.setLongitude(84.7941);
        event.setBreathingDifficulty(false);
        event.setUnconscious(false);

        Long expectedHospitalId = 1L;

        // Act
        HospitalResponseDTO actualResult = hospitalSelectionService.selectHospital(event);

        // Assert
        assertEquals(expectedHospitalId, actualResult.getId());
        verify(bedService).reserveBed(100L);
        verify(hospitalEventProducer).publishHospitalSelectedEvent(any(HospitalSelectedEvent.class));
    }
}