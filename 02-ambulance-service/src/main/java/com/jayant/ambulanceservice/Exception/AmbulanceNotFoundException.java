package com.jayant.ambulanceservice.Exception;

public class AmbulanceNotFoundException extends RuntimeException{

    public AmbulanceNotFoundException(String message){
        super(message);
    }
}
