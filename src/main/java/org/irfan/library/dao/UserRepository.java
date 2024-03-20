package org.irfan.library.dao;

import org.irfan.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameAndEmail(String username, String email);
    boolean existsByUsernameOrEmail(String username, String email);
}
