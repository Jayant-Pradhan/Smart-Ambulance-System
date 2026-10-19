package com.jayant.notificationservice.Client;

import org.springframework.stereotype.Component;

@Component
public class SmsProviderClient {

    public boolean sendSms(String mobileNumber , String message){



        throw new UnsupportedOperationException(
                "SMS provider integration is not configured yet "
        );
    }
}
