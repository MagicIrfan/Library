package org.irfan.library.dao;

import org.irfan.library.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
        Author findByFirstName(String name);
}
