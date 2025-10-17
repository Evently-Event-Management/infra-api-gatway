package com.ticketly.apigateway.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                // We're using our custom corsFilter instead of default cors
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .authorizeExchange(exchange -> exchange
                .pathMatchers(HttpMethod.OPTIONS).permitAll() // Allow CORS preflight requests
                        .pathMatchers("/api/event-query/**").permitAll()
                        .pathMatchers("/api/order/tickets/count").permitAll()
                        .pathMatchers("/api/order/webhook/stripe").permitAll()
                        // Health endpoints for all microservices
                        .pathMatchers("/api/*/health").permitAll()
                        .pathMatchers("/health").permitAll()
                        .anyExchange().authenticated()
                )
                // Configure the gateway as a Resource Server to validate incoming JWTs.
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                // Disable CSRF as we are using a stateless token-based approach.
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }
}
