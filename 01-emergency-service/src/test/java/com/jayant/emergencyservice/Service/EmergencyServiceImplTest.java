package com.jayant.emergencyservice.Service;

import com.jayant.emergencyservice.DTO.Request.CreateEmergencyRequest;
import com.jayant.emergencyservice.DTO.Response.EmergencyResponse;
import com.jayant.emergencyservice.Entity.Emergency;
import com.jayant.emergencyservice.Enum.EmergencyStatus;
import com.jayant.emergencyservice.Enum.EmergencyType;
import com.jayant.emergencyservice.Kafka.EmergencyEventProducer;
import com.jayant.emergencyservice.Mapper.EmergencyMapper;
import com.jayant.emergencyservice.Repository.EmergencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmergencyServiceImplTest {
    @Mock
    EmergencyRepository emergencyRepository;
    @Mock
    EmergencyMapper emergencyMapper;
    @InjectMocks
    EmergencyServiceImpl emergencyService;

    @Mock
    private EmergencyEventProducer emergencyEventProducer;

    @Test
    public void createEmergency_ShouldReturnEmergencyResponse(){

        // Arrange :
        CreateEmergencyRequest request = new CreateEmergencyRequest();
        request.setCallerName("jayant");
        request.setCallerMobile("8114616376");
        request.setEmergencyType(EmergencyType.ACCIDENT);
        request.setPatientCount(2);
        request.setLatitude(20.2961);
        request.setLongitude(85.8245);
        request.setUnconscious(true);
        request.setBreathingDifficulty(false);
        request.setSevereBleeding(false);
        request.setTrappedInVehicle(false);
        request.setAdditionalDetails("road accident near main road ");

        Emergency emergency = new Emergency();

        EmergencyResponse exceptedResponse = new EmergencyResponse();

        when(emergencyMapper.toEntity(request)).thenReturn(emergency);
        when(emergencyRepository.save(emergency)).thenReturn(emergency);
        when(emergencyMapper.toResponse(emergency)).thenReturn(exceptedResponse);

        assertNotNull(emergencyEventProducer);

        // Act
        EmergencyResponse actualResponse = emergencyService.createEmergency(request);

        // Assert
        assertNotNull(actualResponse);
        assertSame(exceptedResponse,actualResponse);

        verify(emergencyMapper).toEntity(request);
        verify(emergencyRepository).save(emergency);
        verify(emergencyMapper).toResponse(emergency);

    }
    @Test
    public void getEmergencyDetailsUsingId_ShouldReturnEmergencyResponse(){

        // Arrange  :
        Long id = 1L;
        Emergency  emergency  = new Emergency();
        EmergencyResponse exceptedResponse = new EmergencyResponse();

        when(emergencyRepository.findById(id)).thenReturn(Optional.of(emergency));

        when(emergencyMapper.toResponse(emergency)).thenReturn(exceptedResponse);

        // Act :
        EmergencyResponse actualResponse = emergencyService.getEmergencyDetailsUsingId(id);

        // Assert :
        assertSame(exceptedResponse,actualResponse);

        verify(emergencyRepository).findById(id);
        verify(emergencyMapper).toResponse(emergency);
    }

    @Test
    public void getAllEmergencyDetails_ShouldReturnEmergencyResponse(){

        // Arrange :
        Emergency emergency = new Emergency();
        EmergencyResponse exceptedResponse = new EmergencyResponse();

        when(emergencyRepository.findAll()).thenReturn(List.of(emergency));
        when(emergencyMapper.toResponse(emergency)).thenReturn(exceptedResponse);

        // Act :
        List<EmergencyResponse> actualResponse = emergencyService.getAllEmergencyDetails();

        // Assert :
        assertSame(exceptedResponse,actualResponse.get(0));

        verify(emergencyRepository).findAll();
        verify(emergencyMapper).toResponse(emergency);
    }

    @Test
    public void cancelEmergency_ShouldReturnNewDetails(){

        // Arrange :
        Long id   = 1L;
        Emergency  emergency  = new Emergency();
        EmergencyResponse exceptedResponse = new EmergencyResponse();

        when(emergencyRepository.findById(id)).thenReturn(Optional.of(emergency));

        when(emergencyRepository.save(emergency)).thenReturn(emergency);

        when(emergencyMapper.toResponse(emergency)).thenReturn(exceptedResponse);

        //Act :
        EmergencyResponse actualResponse = emergencyService.cancelEmergency(id);

        // Assert :
        assertSame(exceptedResponse,actualResponse);
        assertEquals(EmergencyStatus.CANCELLED,emergency.getStatus());
        verify(emergencyRepository).findById(id);
        verify(emergencyMapper).toResponse(emergency);

    }
    @Test
    public void getEmergencyStatus_ShouldReturnStatusOfEmergency(){
        //Arrange :
        Long id = 1L;
        Emergency emergency = new Emergency();
        EmergencyResponse exceptedResponse = new EmergencyResponse();

        when(emergencyRepository.findById(id)).thenReturn(Optional.of(emergency));
        when(emergencyMapper.toResponse(emergency)).thenReturn(exceptedResponse);

        // Act :
        EmergencyResponse actualResponse = emergencyService.getEmergencyStatus(id);

        assertSame(exceptedResponse,actualResponse);

        verify(emergencyRepository).findById(id);
        verify(emergencyMapper).toResponse(emergency);

    }

    @Test
    public void getEmergencyDetailsUsingId_ShouldThrowExceptionWhenNotFound(){
        Long id  = 99L;
        when(emergencyRepository.findById(id)).thenReturn(Optional.empty());
        // Act : and Assert :
        assertThrows(RuntimeException.class,()->{
            emergencyService.getEmergencyDetailsUsingId(id);
        });
        verify(emergencyRepository).findById(id);
    }

    @Test
    public void cancelEmergency_ShouldThrowExceptionWhenNotFound(){
        Long id = 99L;
        when(emergencyRepository.findById(id)).thenReturn(Optional.empty());

        // Act : assert :
        assertThrows(RuntimeException.class,()->{
            emergencyService.cancelEmergency(id);
        });
        verify(emergencyRepository).findById(id);
    }

    @Test
    public void getEmergencyStatus_ShouldThrowExceptionWhenNotFound(){

        Long id = 99L;
        when(emergencyRepository.findById(id)).thenReturn(Optional.empty());

        // Act : assert :
        assertThrows(RuntimeException.class,()->{
            emergencyService.getEmergencyStatus(id);
        });
        verify(emergencyRepository).findById(id);
    }

}
