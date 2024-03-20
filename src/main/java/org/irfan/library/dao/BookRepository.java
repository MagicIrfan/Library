package org.irfan.library.dao;

import jakarta.persistence.criteria.Predicate;
import org.irfan.library.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Integer>, JpaSpecificationExecutor<Book> {
    boolean existsByTitle(String title);
    List<Book> findAllByAuthor_Id(Integer author_id);
    Optional<Book> findByTitle(String title);
    default List<Book> findBooksCustom(Optional<Integer> id, Optional<String> title, Optional<Integer> authorId, Optional<Integer> bookTypeId){
        Specification<Book> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            id.ifPresent(value -> predicates.add(cb.equal(root.get("id"), value)));
            title.ifPresent(value -> predicates.add(cb.like(cb.lower(root.get("title")), "%" + value + "%")));
            authorId.ifPresent(value -> predicates.add(cb.equal(root.get("author").get("id"), value)));
            bookTypeId.ifPresent(value -> predicates.add(cb.equal(root.get("type").get("id"), value)));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return findAll(spec);
    }
}
