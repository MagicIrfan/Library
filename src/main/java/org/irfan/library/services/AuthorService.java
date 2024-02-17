package org.irfan.library.services;

import org.irfan.library.Model.Author;
import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dto.GetAuthorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    // Constructor injection is recommended over field injection
    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<GetAuthorDTO> getAllAuthors() {
        // Use Stream API to map Author objects to GetAuthorDTO and collect to list
        return authorRepository.findAll().stream()
                .map(author -> new GetAuthorDTO(author.getName()))
                .collect(Collectors.toList());
    }

    public Optional<GetAuthorDTO> getAuthorByName(String name) {
        // Simplify the retrieval of an author using Optional.map
        return Optional.ofNullable(authorRepository.findByName(name))
                .map(author -> new GetAuthorDTO(author.getName()));
    }
}
