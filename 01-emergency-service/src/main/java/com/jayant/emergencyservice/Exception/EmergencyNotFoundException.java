package com.jayant.emergencyservice.Exception;

public class EmergencyNotFoundException extends RuntimeException {
    public EmergencyNotFoundException(String message) {
        super(message);
    }
}
