package org.irfan.library.dao;

import org.irfan.library.model.BookType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookTypeRepository extends JpaRepository<BookType, Integer> {
    Optional<BookType> findByName(String name);
    boolean existsByName(String name);
}
