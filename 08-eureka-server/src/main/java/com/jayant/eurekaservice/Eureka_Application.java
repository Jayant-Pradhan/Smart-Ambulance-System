package com.jayant.eurekaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Eureka_Application {

    public static void main(String[] args) {
        SpringApplication.run(Eureka_Application.class, args);
    }

}
