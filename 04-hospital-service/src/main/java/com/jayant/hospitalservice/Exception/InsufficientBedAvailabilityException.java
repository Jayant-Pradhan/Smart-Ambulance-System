package com.jayant.hospitalservice.Exception;

public class InsufficientBedAvailabilityException extends  RuntimeException{

    public InsufficientBedAvailabilityException(String message){
        super(message);
    }
}
