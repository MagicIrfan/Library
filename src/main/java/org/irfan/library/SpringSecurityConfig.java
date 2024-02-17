package org.irfan.library;

import jakarta.annotation.PostConstruct;
import org.irfan.library.Model.User;
import org.irfan.library.dao.RoleRepository;
import org.irfan.library.dao.UserRepository;
import org.irfan.library.filters.JwtTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Configuration
@Component
@EnableWebSecurity
public class SpringSecurityConfig{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Bean
    public JwtTokenAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtTokenAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                if (rawPassword.length() < 5)
                    throw new RuntimeException("Mot de passe trop court");
                return super.encode(rawPassword);
            }
        };
    }

    @PostConstruct
    public void init() {
        User user = new User(null,roleRepository.findByName("ADMIN"),"Irfan","root@root.com",passwordEncoder().encode("saucisse"));
        userRepository.save(user);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/auth/**").permitAll() // Permettre l'accès non authentifié à certaines routes
                                .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Utiliser une politique de session sans état
                ).addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
