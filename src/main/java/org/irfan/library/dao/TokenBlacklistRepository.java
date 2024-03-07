package org.irfan.library.dao;

import org.irfan.library.Model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Integer> {
    Optional<TokenBlacklist> findByToken(String token);
    boolean existsByToken(String token);
}
