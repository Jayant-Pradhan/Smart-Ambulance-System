package com.jayant.emergencyservice.Exception;

public class DuplicateEmergencyException extends RuntimeException{
    public DuplicateEmergencyException(String message){
        super(message);
    }
}
