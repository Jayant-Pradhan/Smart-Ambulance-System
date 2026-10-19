package com.jayant.hospitalservice.Service;


import com.jayant.hospitalservice.DTO.HospitalRequestDTO;

import com.jayant.hospitalservice.DTO.HospitalResponseDTO;
import com.jayant.hospitalservice.Entity.Hospital;
import com.jayant.hospitalservice.Exception.ResourceNotFoundException;
import com.jayant.hospitalservice.Repository.HospitalRepository;
import com.jayant.hospitalservice.Service.Implementation.HospitalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HospitalServiceImplTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private HospitalServiceImpl hospitalService;

    @Test
    void createHospital_shouldSaveAndReturnHospital() {
        // Arrange
        HospitalRequestDTO request = new HospitalRequestDTO();
        request.setName("City Care Hospital");
        request.setAddress("Main Road, Berhampur");
        request.setCity("Berhampur");
        request.setLatitude(19.3149);
        request.setLongitude(84.7941);

        Hospital savedHospital = new Hospital();
        savedHospital.setId(1L);
        savedHospital.setName("City Care Hospital");
        savedHospital.setCity("Berhampur");
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(savedHospital);

        // Act
        HospitalResponseDTO actualResult = hospitalService.createHospital(request);

        // Assert
        assertEquals(1L, actualResult.getId());
        assertEquals("City Care Hospital", actualResult.getName());
        assertEquals("Berhampur", actualResult.getCity());
        verify(hospitalRepository).save(any(Hospital.class));
    }

    @Test
    void getHospitalById_whenFound_shouldReturnHospital() {
        // Arrange
        Hospital hospital = new Hospital();
        hospital.setId(1L);
        hospital.setName("City Care Hospital");
        hospital.setCity("Berhampur");
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));

        // Act
        HospitalResponseDTO actualResult = hospitalService.getHospitalById(1L);

        // Assert
        assertEquals(1L, actualResult.getId());
        assertEquals("City Care Hospital", actualResult.getName());
        assertEquals("Berhampur", actualResult.getCity());
    }

    @Test
    void getHospitalById_whenNotFound_shouldThrowException() {
        // Arrange
        when(hospitalRepository.findById(99L)).thenReturn(Optional.empty());
        String expectedMessage = "hospital id is not found with this id : 99";

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> hospitalService.getHospitalById(99L));
        String actualMessage = exception.getMessage();

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getAllHospitals_shouldReturnAllHospitals() {
        // Arrange
        Hospital hospitalOne = new Hospital();
        hospitalOne.setId(1L);
        hospitalOne.setName("City Care Hospital");

        Hospital hospitalTwo = new Hospital();
        hospitalTwo.setId(2L);
        hospitalTwo.setName("Apollo Hospital");

        List<Hospital> hospitals = new ArrayList<>();
        hospitals.add(hospitalOne);
        hospitals.add(hospitalTwo);
        when(hospitalRepository.findAll()).thenReturn(hospitals);

        // Act
        List<HospitalResponseDTO> actualResult = hospitalService.getAllHospitals();

        // Assert
        assertEquals(2, actualResult.size());
        assertEquals("City Care Hospital", actualResult.get(0).getName());
        assertEquals("Apollo Hospital", actualResult.get(1).getName());
    }

    @Test
    void updateHospital_whenFound_shouldUpdateCity() {
        // Arrange
        Hospital existingHospital = new Hospital();
        existingHospital.setId(1L);
        existingHospital.setName("City Care Hospital");
        existingHospital.setCity("Berhampur");
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(existingHospital));
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(existingHospital);

        HospitalRequestDTO request = new HospitalRequestDTO();
        request.setCity("Bhubaneswar");

        // Act
        HospitalResponseDTO actualResult = hospitalService.updateHospital(1L, request);

        // Assert
        assertEquals("Bhubaneswar", actualResult.getCity());
        assertEquals("City Care Hospital", actualResult.getName());
    }

    @Test
    void updateHospital_whenNotFound_shouldThrowException() {
        // Arrange
        when(hospitalRepository.findById(99L)).thenReturn(Optional.empty());
        HospitalRequestDTO request = new HospitalRequestDTO();
        request.setCity("Bhubaneswar");
        String expectedMessage = "hospital id is not found with this id :  99";

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> hospitalService.updateHospital(99L, request));
        String actualMessage = exception.getMessage();

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deactivateHospital_whenFound_shouldReturnTrue() {
        // Arrange
        Hospital hospital = new Hospital();
        hospital.setId(1L);
        hospital.setActive(true);
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));
        boolean expectedResult = true;

        // Act
        boolean actualResult = hospitalService.deactivateHospital(1L);

        // Assert
        assertEquals(expectedResult, actualResult);

    }
}