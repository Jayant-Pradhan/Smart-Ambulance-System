package com.jayant.notificationservice.Client;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailProviderClient {

    private final JavaMailSender javaMailSender;

    public EmailProviderClient(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String recipientEmail , String subject  , String message){

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("sarojpradhan2668@gmail.com");
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);


    }
}
