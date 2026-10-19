package com.jayant.trackingservice.Exception;

public class TrackingNotFoundException extends RuntimeException{

    public TrackingNotFoundException(String message){
        super(message);
    }
}
