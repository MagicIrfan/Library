package org.irfan.library.dao;

import jakarta.persistence.criteria.Predicate;
import org.irfan.library.Model.Author;
import org.irfan.library.Model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer>, JpaSpecificationExecutor<Author> {
        Optional<Author> findByFirstnameAndLastname(String firstName, String lastName);
        boolean existsByFirstnameAndLastname(String firstName, String lastName);
        default List<Author> findAuthorsCustom(Optional<Long> id, Optional<String> firstname, Optional<String> lastname, Optional<Long> bookId){
                Specification<Author> spec = (root, query, cb) -> {
                        List<Predicate> predicates = new ArrayList<>();

                        id.ifPresent(value -> predicates.add(cb.equal(root.get("id"), value)));
                        firstname.ifPresent(value -> predicates.add(cb.like(cb.lower(root.get("firstname")), "%" + value + "%")));
                        lastname.ifPresent(value -> predicates.add(cb.like(cb.lower(root.get("lastname")), "%" + value + "%")));
                        bookId.ifPresent(value -> predicates.add(cb.equal(root.get("book").get("id"), value)));

                        return cb.and(predicates.toArray(new Predicate[0]));
                };

                return findAll(spec);
        }
}
