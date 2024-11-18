package com.example.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main application class for the Payment application.
 * This class serves as the entry point for the Spring Boot application.
 */
@SpringBootApplication
@RestController
public class Payment {

    /**
     * Main method to launch the Payment application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Payment.class, args);
    }


}
