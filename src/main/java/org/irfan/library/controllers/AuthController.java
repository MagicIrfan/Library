package org.irfan.library.controllers;

import jakarta.validation.Valid;
import org.irfan.library.dto.LoginRequest;
import org.irfan.library.dto.SignupRequest;
import org.irfan.library.response.JwtResponse;
import org.irfan.library.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
@RestController
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value="/login", consumes = {"application/json"})
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        try {
            String jwt = authService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok().body(new JwtResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Échec de l'authentification : " + e.getMessage());
        }   catch (Exception e) {
            return ResponseEntity.internalServerError().body("Une erreur est survenue : " + e.getMessage());
        }
    }

    @PostMapping(value="/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        authService.signUp(request.getUsername(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok().body("L'utilisateur est inscrit");
    }
}
