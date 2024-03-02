package org.irfan.library.dao;

import org.irfan.library.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
        Optional<Author> findByFirstname(String name);
}
