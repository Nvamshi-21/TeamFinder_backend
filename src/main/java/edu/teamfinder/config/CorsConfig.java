package edu.teamfinder.config;

import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean CorsFilter corsFilter(@Value("${app.cors.allowed-origins}") String allowed) {
        CorsConfiguration c=new CorsConfiguration(); c.setAllowedOrigins(Arrays.stream(allowed.split(",")).map(String::trim).toList()); c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS")); c.setAllowedHeaders(List.of("Authorization","Content-Type")); c.setExposedHeaders(List.of("Authorization")); c.setAllowCredentials(false); c.setMaxAge(3600L);
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source=new org.springframework.web.cors.UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",c); return new CorsFilter(source);
    }
}
