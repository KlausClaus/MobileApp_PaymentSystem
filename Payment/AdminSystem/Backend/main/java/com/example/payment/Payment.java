package com.example.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController

public class Payment {

    public static void main(String[] args) {
        SpringApplication.run(Payment.class, args);
    }


}
