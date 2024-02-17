package org.irfan.library.controllers;

import org.irfan.library.components.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        try {
            // Tentative d'authentification
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // Si l'authentification est réussie, SecurityContext est mis à jour
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Génération du token JWT
            String jwt = tokenProvider.createToken(username);

            // Retourne le token JWT dans la réponse
            return ResponseEntity.ok().body(jwt);
        } catch (AuthenticationException e) {
            // Gestion de l'erreur d'authentification
            return ResponseEntity.badRequest().body("Échec de l'authentification : " + e.getMessage());
        }
    }
}
