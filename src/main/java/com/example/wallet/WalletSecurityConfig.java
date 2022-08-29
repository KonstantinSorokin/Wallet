package com.example.wallet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Sets up the security configuration of the Wallet Service.
 * Turns on basic authentication and turns off browser cache.
 */
@Configuration
@EnableWebSecurity
public class WalletSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers.cacheControl(cache -> cache.disable()))
            .authorizeHttpRequests((auth) -> auth.anyRequest().authenticated())
            .httpBasic();
        return http.build();
    }

}
