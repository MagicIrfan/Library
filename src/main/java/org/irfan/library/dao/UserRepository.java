package org.irfan.library.dao;

import jakarta.persistence.criteria.Predicate;
import org.irfan.library.Model.Book;
import org.irfan.library.Model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameAndEmail(String username, String email);
    boolean existsByUsernameOrEmail(String username, String email);
}
