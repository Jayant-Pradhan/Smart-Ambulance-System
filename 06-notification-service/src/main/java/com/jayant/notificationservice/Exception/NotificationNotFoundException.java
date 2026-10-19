package com.jayant.notificationservice.Exception;

public class NotificationNotFoundException extends  RuntimeException{

    public NotificationNotFoundException(String message){
        super(message);
    }
}
