package com.example.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration class for handling Cross-Origin Resource Sharing (CORS) settings.
 * This configuration allows the application to handle requests from different origins
 * by setting specific headers, methods, and origins that are permitted.
 */
@Configuration
public class CorsConfig {

    /**
     * Maximum validity period for cross-origin requests, set to 1 day.
     */
    private static final long MAX_AGE = 24 * 60 * 60;

    /**
     * Bean to configure and provide a {@link CorsFilter} for handling CORS requests.
     *
     * @return A configured {@link CorsFilter} instance.
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8080"); // 1 Set allowed origin
        corsConfiguration.addAllowedHeader("*"); // 2 Set allowed request headers
        corsConfiguration.addAllowedMethod("*"); // 3 Set allowed request methods
        corsConfiguration.setMaxAge(MAX_AGE);
        source.registerCorsConfiguration("/**", corsConfiguration); // 4 Configure CORS settings for all endpoints
        return new CorsFilter(source);
    }
}
