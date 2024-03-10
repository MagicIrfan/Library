package org.irfan.library.services;

import org.irfan.library.Model.TokenBlacklist;
import org.irfan.library.dao.TokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenBlacklistService {
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    public TokenBlacklistService(TokenBlacklistRepository tokenBlacklistRepository){
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Transactional(readOnly = true)
    public boolean existsByToken(String token){
        return tokenBlacklistRepository.existsByToken(token);
    }

    public void addTokenOnBlackList(TokenBlacklist token){
        tokenBlacklistRepository.save(token);
    }
}
