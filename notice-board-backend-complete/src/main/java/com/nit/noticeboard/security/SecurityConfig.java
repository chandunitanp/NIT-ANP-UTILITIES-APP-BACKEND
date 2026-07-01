package com.nit.noticeboard.security;

import java.util.Arrays;
import java.util.Collections;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nit.noticeboard.service.JwtService;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // =========================================================
    // JWT SERVICE
    // =========================================================

    private final JwtService jwtService;

    public SecurityConfig(
            JwtService jwtService
    ) {

        this.jwtService = jwtService;
    }

    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

            // =================================================
            // DISABLE CSRF
            // =================================================

            .csrf(csrf -> csrf.disable())

            // =================================================
            // ENABLE CORS
            // =================================================

            .cors(cors ->
                    cors.configurationSource(
                            corsConfigurationSource()
                    )
            )

            // =================================================
            // STATELESS SESSION
            // JWT BASED AUTH
            // =================================================

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            // =================================================
            // PUBLIC ROUTES
            // =================================================

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(

                        // AUTH APIs
                        "/auth/register",
                        "/auth/login",
                        "/auth/forgot-password",
                        "/auth/reset-password",

                        // SWAGGER
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",

                        // FILES
                        "/files/**"

                ).permitAll()

                // =================================================
                // PROTECTED ROUTES
                // =================================================

                .anyRequest().authenticated()
            )

            // =================================================
            // JWT RESOURCE SERVER
            // =================================================

            .oauth2ResourceServer(oauth2 -> oauth2

                .jwt(jwt ->
                        jwt.jwtAuthenticationConverter(
                                jwtAuthConverter()
                        )
                )
            );

        return http.build();
    }

    // =========================================================
    // JWT DECODER
    // =========================================================

    @Bean
    public JwtDecoder jwtDecoder() {

        SecretKey key = jwtService.signingKey();

        return NimbusJwtDecoder
                .withSecretKey(key)
                .build();
    }

    // =========================================================
    // ROLE CONVERTER
    // =========================================================

    @Bean
    public JwtAuthenticationConverter jwtAuthConverter() {

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            String role =
                    jwt.getClaimAsString("role");

            // =============================================
            // NO ROLE
            // =============================================

            if (role == null) {

                return Collections.emptyList();
            }

            // =============================================
            // ROLE FORMAT:
            // ROLE_ADMIN
            // ROLE_STUDENT
            // ROLE_FACULTY
            // =============================================

            return Collections.singletonList(

                    new SimpleGrantedAuthority(
                            "ROLE_" + role
                    )
            );
        });

        return converter;
    }

    // =========================================================
    // CORS CONFIGURATION
    // =========================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config =
                new CorsConfiguration();

        // =================================================
        // ALLOW ALL ORIGINS
        // GOOD FOR EXPO / MOBILE
        // =================================================

        config.setAllowedOriginPatterns(
                Arrays.asList("*")
        );

        // =================================================
        // ALLOW METHODS
        // =================================================

        config.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        // =================================================
        // ALLOW HEADERS
        // =================================================

        config.setAllowedHeaders(
                Arrays.asList("*")
        );

        // =================================================
        // JWT AUTH DOES NOT NEED COOKIES
        // =================================================

        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                config
        );

        return source;
    }

    // =========================================================
    // PASSWORD ENCODER
    // BCrypt PASSWORD HASHING
    // =========================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}