package org.irfan.library.components;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String createToken(String username) {
        // Construisez le token JWT
        return Jwts.builder()
                .setSubject(username) // Définissez le sujet du token (nom d'utilisateur)
                .setIssuedAt(new Date()) // Date d'émission du token
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs)) // Date d'expiration
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // Algorithme de signature et clé secrète
                .compact(); // Construisez le JWT et sérialisez-le en une chaîne compacte
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // Méthode pour valider le token JWT
    public boolean validateToken(String token) {
        try {
            // Parse le token.
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);

            // Vérifie si le token a expiré.
            if (claims.getBody().getExpiration().before(new Date())) {
                return false; // Le token a expiré.
            }

            return true; // Le token est valide.
        } catch (SignatureException e) {
            // Logique de gestion des exceptions pour une signature JWT invalide.
            // Vous pouvez logger cette exception et/ou traiter selon les besoins de votre application.
            return false; // Le token est invalide.
        } catch (Exception e) {
            // Gérer d'autres exceptions qui pourraient survenir lors du parsing du token.
            return false;
        }
    }
}