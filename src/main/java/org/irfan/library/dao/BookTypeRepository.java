package org.irfan.library.dao;

import org.irfan.library.Model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookTypeRepository extends JpaRepository<Type, Integer> {
    Optional<Type> findByName(String name);
    boolean existsByName(String name);
}
