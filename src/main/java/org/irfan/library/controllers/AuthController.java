package org.irfan.library.controllers;

import org.irfan.library.components.JwtTokenProvider;
import org.irfan.library.dao.UserRepository;
import org.irfan.library.dto.LoginRequest;
import org.irfan.library.dto.RoleDTO;
import org.irfan.library.dto.SignupRequest;
import org.irfan.library.enums.RoleEnum;
import org.irfan.library.response.JwtResponse;
import org.irfan.library.services.AuthService;
import org.irfan.library.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.irfan.library.Model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value="/login", consumes = {"application/json"})
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest request) throws Exception {
        try {
            String jwt = authService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok().body(new JwtResponse(jwt));
        } catch (AuthenticationException e) {
            // Gestion de l'erreur d'authentification
            return ResponseEntity.badRequest().body("Échec de l'authentification : " + e.getMessage());
        }   catch (Exception e) {
            return ResponseEntity.internalServerError().body("Une erreur est survenue : " + e.getMessage());
        }
    }

    @PostMapping(value="/signup", consumes = {"application/json"})
    public ResponseEntity<?> signup(@Validated @RequestBody SignupRequest request) {
        authService.signUp(request.getUsername(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok().body("L'utilisateur est inscrit");
    }
}
