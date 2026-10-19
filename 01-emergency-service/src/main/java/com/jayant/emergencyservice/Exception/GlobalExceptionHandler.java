package com.jayant.emergencyservice.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmergencyNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleEmergencyNotFound(EmergencyNotFoundException exception){

        Map<String,String> error = Map.of("error" , "Emergency Not Found" , "message" ,exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DuplicateEmergencyException.class)
    public ResponseEntity<Map<String,String>> handleDuplicateEmergency(DuplicateEmergencyException exception){
        Map<String,String> error =  Map.of("error" ,"Duplicate Emergency " , "message" , exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
