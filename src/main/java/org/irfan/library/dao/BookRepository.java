package org.irfan.library.dao;

import org.irfan.library.Model.Author;
import org.irfan.library.Model.Book;
import org.irfan.library.Model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Integer> {
    Optional<List<Book>> findAllByAuthor(Author author);
    Optional<Book> findByTitle(String title);
    Optional<List<Book>> findAllByType(Type type);
}
