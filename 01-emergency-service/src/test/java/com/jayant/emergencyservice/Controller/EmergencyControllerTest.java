package com.jayant.emergencyservice.Controller;

import com.jayant.emergencyservice.DTO.Request.CreateEmergencyRequest;
import com.jayant.emergencyservice.DTO.Response.EmergencyResponse;
import com.jayant.emergencyservice.Service.EmergencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmergencyControllerTest {
    @Mock
    EmergencyService emergencyService;

    @InjectMocks
    EmergencyController emergencyController;

    @Test
    public void createEmergency_ShouldReturn201Created(){
        // Arrange :
        CreateEmergencyRequest request = new CreateEmergencyRequest();
        EmergencyResponse  exceptedResponse = new EmergencyResponse();

        when(emergencyService.createEmergency(request)).thenReturn(exceptedResponse);

        // Act :
        ResponseEntity<EmergencyResponse> actualResponse = emergencyController.createEmergency(request);

        // Assert :
        assertEquals(HttpStatus.CREATED,actualResponse.getStatusCode());
        assertSame(exceptedResponse,actualResponse.getBody());

        verify(emergencyService).createEmergency(request);


    }

    @Test
    public void getEmergencyDetailsUsingId_ShouldReturnEmergencyResponse(){

        //Arrange :
        Long id =  2L;
        EmergencyResponse exceptedResponse = new EmergencyResponse();

        when(emergencyService.getEmergencyDetailsUsingId(id)).thenReturn(exceptedResponse);

        // Act :
        ResponseEntity<EmergencyResponse> actualResponse = emergencyController.getEmergencyDetailsUsingId(id);

        // Assert :
        assertEquals(HttpStatus.OK,actualResponse.getStatusCode());
        assertSame(exceptedResponse,actualResponse.getBody());
        verify(emergencyService).getEmergencyDetailsUsingId(id);
    }

    @Test
    public void getAllEmergencies_ShouldReturnEmergencyList(){
        EmergencyResponse  exceptedResponse = new EmergencyResponse();
        when(emergencyService.getAllEmergencyDetails()).thenReturn(List.of(exceptedResponse));
        // Act :
        ResponseEntity<List<EmergencyResponse>> actualResponse = emergencyController.getAllEmergencies();
        // Assert :
        assertEquals(1,actualResponse.getBody().size());
        assertSame(exceptedResponse,actualResponse.getBody().get(0));
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        verify(emergencyService).getAllEmergencyDetails();
    }

    @Test
    public void cancelEmergency_ShouldReturnCancelEmergency(){
        Long id  = 3L;

        EmergencyResponse exceptedResponse = new EmergencyResponse();

        when(emergencyService.cancelEmergency(id)).thenReturn(exceptedResponse);

        ResponseEntity<EmergencyResponse> actualResponse = emergencyController.cancelEmergency(id);

        // Assert :
        assertSame(exceptedResponse,actualResponse.getBody());

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        verify(emergencyService).cancelEmergency(id);
    }

    @Test
    public void getEmergencyStatus_ShouldReturn(){
        Long id  = 5L;
        EmergencyResponse exceptedResponse = new EmergencyResponse();
        when(emergencyService.getEmergencyStatus(id)).thenReturn(exceptedResponse);

        // Act
        ResponseEntity<EmergencyResponse> actualResponse = emergencyController.getEmergencyStatus(id);

        // Assert :
        assertSame(exceptedResponse,actualResponse.getBody());

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        verify(emergencyService).getEmergencyStatus(id);
    }













}
