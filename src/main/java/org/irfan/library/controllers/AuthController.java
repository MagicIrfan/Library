package org.irfan.library.controllers;

import org.irfan.library.components.JwtTokenProvider;
import org.irfan.library.dao.UserRepository;
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
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String name, @RequestParam String password) throws Exception {
        try {
            // Tentative d'authentification
            authenticate(name, password);

            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(name);

            // Génération du token JWT
            String jwt = tokenProvider.createToken(userDetails.getUsername());

            // Retourne le token JWT dans la réponse
            return ResponseEntity.ok().body(jwt);
        } catch (AuthenticationException e) {
            // Gestion de l'erreur d'authentification
            return ResponseEntity.badRequest().body("Échec de l'authentification : " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(){
        User user = new User("oui","oui",bCryptPasswordEncoder.encode("non"));
        userRepository.save(user);
        return ResponseEntity.ok().body("Inscrit");
    }

    @GetMapping("/saucisse")
    public String saucisse(){
        return "Saucisse";
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
