package org.irfan.library.services;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.irfan.library.dao.TokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtTokenService {

    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;
    private TokenBlacklistRepository tokenBlacklistRepository;
    @Autowired
    public JwtTokenService(TokenBlacklistRepository tokenBlacklistRepository){
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date()) && !tokenBlacklistRepository.existsByToken(token);
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }
}