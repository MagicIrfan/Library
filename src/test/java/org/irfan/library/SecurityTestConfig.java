package org.irfan.library;

import org.irfan.library.enums.RoleEnum;
import org.irfan.library.filters.JwtTokenAuthenticationFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@Profile("test")
public class SecurityTestConfig{

    @Bean
    public JwtTokenAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtTokenAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers(HttpMethod.PATCH,"/api/v1/**").hasAuthority(RoleEnum.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT,"/api/v1/**").hasAuthority(RoleEnum.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/**").hasAuthority(RoleEnum.ADMIN.name())
                                .requestMatchers(HttpMethod.POST,"/api/v1/logout").hasAnyAuthority(RoleEnum.ADMIN.name(),RoleEnum.USER.name())
                                .requestMatchers("/api/v1/signup", "/api/v1/login", "/api/v1/refreshToken", "/swagger-ui/**",  "/swagger-resources/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}