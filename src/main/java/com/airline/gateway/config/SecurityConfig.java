package com.airline.gateway.config;


import com.airline.gateway.security.JwtAccessDeniedHandler;
import com.airline.gateway.security.JwtAuthenticationConverter;
import com.airline.gateway.security.JwtAuthenticationEntryPoint;
import com.airline.gateway.security.JwtAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final JwtAuthenticationConverter converter;
    @Autowired
    private final JwtAuthenticationManager manager;
    @Autowired
    private final JwtAuthenticationEntryPoint entryPoint;
    @Autowired
    private final JwtAccessDeniedHandler deniedHandler;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        AuthenticationWebFilter filter =
                new AuthenticationWebFilter(manager);

        filter.setServerAuthenticationConverter(converter);
        System.out.println("Inside SecurityConfig securityWebFilterChain");
        return http

                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(deniedHandler)
                )

                .authorizeExchange(auth -> auth

                        .pathMatchers(
                                "/auth/login",
                                "/auth/register",
                                "/auth/refresh-token"
                        ).permitAll()
                        // Flight APIs
                        .pathMatchers(HttpMethod.GET, "/api/v1/flights/**")
                        .permitAll()

                        .pathMatchers(HttpMethod.POST, "/api/v1/flights/**")
                        .hasAnyRole("ADMIN", "AIRLINE_MANAGER")

                        .pathMatchers(HttpMethod.PUT, "/api/v1/flights/**")
                        .hasAnyRole("ADMIN", "AIRLINE_MANAGER")

                        // Booking APIs
                        .pathMatchers(HttpMethod.POST, "/bookings/**")
                        .hasRole("CUSTOMER")

                        .pathMatchers(HttpMethod.GET, "/bookings/**")
                        .authenticated()

                        .pathMatchers(HttpMethod.DELETE, "/bookings/**")
                        .hasRole("CUSTOMER")

                        // User APIs
                        .pathMatchers(HttpMethod.GET, "/users/**")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        .anyExchange()
                        .authenticated()

                )

                .addFilterAt(
                        filter,
                        SecurityWebFiltersOrder.AUTHENTICATION
                )

                .build();
    }

}