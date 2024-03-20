package org.irfan.library.controllers;

import jakarta.validation.Valid;
import org.irfan.library.model.RefreshToken;
import org.irfan.library.dto.JwtResponseDTO;
import org.irfan.library.dto.request.LoginRequest;
import org.irfan.library.dto.request.LogoutRequest;
import org.irfan.library.dto.request.RefreshTokenRequest;
import org.irfan.library.dto.response.OKMessageResponse;
import org.irfan.library.dto.request.SignupRequest;
import org.irfan.library.services.AuthService;
import org.irfan.library.services.JwtTokenService;
import org.irfan.library.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;
    @Autowired
    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtTokenService jwtTokenService){
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenService = jwtTokenService;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        try {
            String jwt = authService.login(request.getUsername(), request.getPassword());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getUsername());
            return ResponseEntity.ok().body(JwtResponseDTO.builder()
                    .accessToken(jwtTokenService.createToken(request.getUsername()))
                    .token(refreshToken.getToken())
                    .build());
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Échec de l'authentification : " + e.getMessage());
        }   catch (Exception e) {
            return ResponseEntity.internalServerError().body("Une erreur est survenue : " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequest request){
        authService.logout(request);
        return ResponseEntity.ok().body("Utilisateur déconnecté");
    }

    @PostMapping("/signup")
    public ResponseEntity<OKMessageResponse> signup(@Valid @RequestBody SignupRequest request) {
        authService.signUp(request.getUsername(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok().body(new OKMessageResponse<>("L'utilisateur est inscrit"));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtTokenService.createToken(userInfo.getUsername());
                    JwtResponseDTO responseDTO = JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken())
                            .build();
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                })
                .orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));
    }
}
