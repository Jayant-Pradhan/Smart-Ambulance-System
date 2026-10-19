package com.jayant.ambulanceservice.Service;

import com.jayant.ambulanceservice.DTO.Request.RegisterAmbulanceRequest;
import com.jayant.ambulanceservice.DTO.Response.AmbulanceResponse;
import com.jayant.ambulanceservice.Entity.Ambulance;
import com.jayant.ambulanceservice.Enum.AmbulanceStatus;
import com.jayant.ambulanceservice.Exception.AmbulanceNotFoundException;
import com.jayant.ambulanceservice.Exception.DuplicateAmbulanceException;
import com.jayant.ambulanceservice.Kafka.AmbulanceEventProducer;
import com.jayant.ambulanceservice.Mapper.AmbulanceMapper;
import com.jayant.ambulanceservice.Repository.AmbulanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AmbulanceServiceImplTest {

    @Mock
    private AmbulanceRepository ambulanceRepository;

    @Mock
    private AmbulanceMapper ambulanceMapper;

    @Mock
    private AmbulanceEventProducer ambulanceEventProducer;

    @InjectMocks
    private AmbulanceServiceImpl ambulanceService;

    @Test
    void updateAmbulanceStatus_ShouldReturnResponse_WhenAmbulanceExists() {

        // Arrange
        Long id = 2L;
        AmbulanceStatus newStatus = AmbulanceStatus.AVAILABLE;
        Ambulance ambulance = new Ambulance();
        ambulance.setId(id);
        AmbulanceResponse exceptedResponse = new AmbulanceResponse();

        when(ambulanceRepository.findById(id)).thenReturn(Optional.of(ambulance));
        when(ambulanceRepository.save(ambulance)).thenReturn(ambulance);
        when(ambulanceMapper.toResponse(ambulance)).thenReturn(exceptedResponse);

        // Act
        AmbulanceResponse actualResponse = ambulanceService.updateAmbulanceStatus(id, newStatus);

        // Assert
        assertSame(exceptedResponse, actualResponse);
        assertEquals(newStatus, ambulance.getStatus());

        // Verify
        verify(ambulanceRepository).findById(id);
        verify(ambulanceRepository).save(ambulance);
        verify(ambulanceMapper).toResponse(ambulance);
        verify(ambulanceEventProducer).publishAmbulanceStatusChanged(any());
    }

    @Test
    void updateAmbulanceStatus_ShouldThrowException_WhenAmbulanceNotFound() {

        Long id = 99L;
        when(ambulanceRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                AmbulanceNotFoundException.class,
                () -> ambulanceService.updateAmbulanceStatus(id, AmbulanceStatus.AVAILABLE)
        );

        // Verify
        verify(ambulanceRepository).findById(id);
        verify(ambulanceRepository, never()).save(any(Ambulance.class));
        verify(ambulanceMapper, never()).toResponse(any(Ambulance.class));
        verifyNoInteractions(ambulanceEventProducer);
    }

    @Test
    void registrationAmbulance_ShouldReturnResponse_WhenRegistrationIsNew() {

        // Arrange
        RegisterAmbulanceRequest request = new RegisterAmbulanceRequest();
        request.setRegistrationNumber("OD02AB1234");

        Ambulance ambulance = new Ambulance();
        AmbulanceResponse exceptedResponse = new AmbulanceResponse();

        when(ambulanceRepository.existsByRegistrationNumber("OD02AB1234")).thenReturn(false);
        when(ambulanceMapper.toEntity(request)).thenReturn(ambulance);
        when(ambulanceRepository.save(ambulance)).thenReturn(ambulance);
        when(ambulanceMapper.toResponse(ambulance)).thenReturn(exceptedResponse);

        // Act
        AmbulanceResponse actualResponse = ambulanceService.registrationAmbulance(request);

        // Assert
        assertSame(exceptedResponse, actualResponse);

        // Verify
        verify(ambulanceRepository).existsByRegistrationNumber("OD02AB1234");
        verify(ambulanceMapper).toEntity(request);
        verify(ambulanceRepository).save(ambulance);
        verify(ambulanceMapper).toResponse(ambulance);
        verify(ambulanceEventProducer).publishAmbulanceRegistered(any());
    }

    @Test
    void registrationAmbulance_ShouldThrowException_WhenRegistrationAlreadyExists() {

        // Arrange
        RegisterAmbulanceRequest request = new RegisterAmbulanceRequest();
        request.setRegistrationNumber("OD02AB6666");

        when(ambulanceRepository.existsByRegistrationNumber("OD02AB6666")).thenReturn(true);

        // Act + Assert
        assertThrows(
                DuplicateAmbulanceException.class,
                () -> ambulanceService.registrationAmbulance(request)
        );

        // Verify
        verify(ambulanceRepository).existsByRegistrationNumber("OD02AB6666");
        verify(ambulanceMapper, never()).toEntity(any(RegisterAmbulanceRequest.class));
        verify(ambulanceRepository, never()).save(any(Ambulance.class));
        verifyNoInteractions(ambulanceEventProducer);
    }

    @Test
    void getAmbulanceById_ShouldReturnResponse_WhenAmbulanceExists() {

        Long id = 3L;
        Ambulance ambulance = new Ambulance();
        ambulance.setId(id);
        AmbulanceResponse exceptedResponse = new AmbulanceResponse();

        when(ambulanceRepository.findById(id)).thenReturn(Optional.of(ambulance));
        when(ambulanceMapper.toResponse(ambulance)).thenReturn(exceptedResponse);

        // Act
        AmbulanceResponse actualResponse = ambulanceService.getAmbulanceById(id);

        // Assert
        assertSame(exceptedResponse, actualResponse);

        // Verify
        verify(ambulanceRepository).findById(id);
        verify(ambulanceMapper).toResponse(ambulance);
    }

    @Test
    void getAmbulanceById_ShouldThrowException_WhenAmbulanceNotFound() {

        // Arrange
        Long id = 99L;
        when(ambulanceRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                AmbulanceNotFoundException.class,
                () -> ambulanceService.getAmbulanceById(id)
        );

        // Verify
        verify(ambulanceRepository).findById(id);
        verify(ambulanceMapper, never()).toResponse(any(Ambulance.class));
    }

    @Test
    void getAllAmbulances_ShouldReturnList_WhenAmbulancesExist() {

        // Arrange
        Ambulance ambulance1 = new Ambulance();
        ambulance1.setId(1L);

        Ambulance ambulance2 = new Ambulance();
        ambulance2.setId(2L);

        List<Ambulance> ambulances = List.of(ambulance1, ambulance2);

        AmbulanceResponse response1 = new AmbulanceResponse();
        AmbulanceResponse response2 = new AmbulanceResponse();

        when(ambulanceRepository.findAll()).thenReturn(ambulances);
        when(ambulanceMapper.toResponse(ambulance1)).thenReturn(response1);
        when(ambulanceMapper.toResponse(ambulance2)).thenReturn(response2);

        // Act
        List<AmbulanceResponse> actualResponses = ambulanceService.getAllAmbulances();

        // Assert
        assertEquals(2, actualResponses.size());
        assertSame(response1, actualResponses.get(0));
        assertSame(response2, actualResponses.get(1));

        // Verify
        verify(ambulanceRepository).findAll();
        verify(ambulanceMapper).toResponse(ambulance1);
        verify(ambulanceMapper).toResponse(ambulance2);
    }

    @Test
    void getAllAmbulances_ShouldReturnEmptyList_WhenNoAmbulancesExist() {

        // Arrange
        when(ambulanceRepository.findAll()).thenReturn(List.of());

        // Act
        List<AmbulanceResponse> actualResponses = ambulanceService.getAllAmbulances();

        // Assert
        assertNotNull(actualResponses);
        assertTrue(actualResponses.isEmpty());

        // Verify
        verify(ambulanceRepository).findAll();
        verifyNoInteractions(ambulanceMapper);
    }
}