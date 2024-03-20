package org.irfan.library.services;

import org.irfan.library.model.RefreshToken;
import org.irfan.library.model.User;
import org.irfan.library.dao.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    @Value("${app.refreshTokenExpirationMs}")
    private int refreshTokenExpirationMs;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserService userService){
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    public RefreshToken createRefreshToken(String username){
        User user = userService.getUserEntityByUsername(username);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpirationMs))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
