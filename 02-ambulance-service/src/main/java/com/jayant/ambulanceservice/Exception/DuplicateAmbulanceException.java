package com.jayant.ambulanceservice.Exception;

public class DuplicateAmbulanceException extends RuntimeException{

    public DuplicateAmbulanceException(String message){
        super(message);
    }
}
