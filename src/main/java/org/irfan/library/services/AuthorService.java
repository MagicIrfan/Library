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

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<GetAuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(author -> new GetAuthorDTO(author.getFirstname()))
                .collect(Collectors.toList());
    }

    public Optional<GetAuthorDTO> getAuthorByName(String name) {
        return authorRepository.findByFirstname(name)
                .map(author -> new GetAuthorDTO(author.getFirstname()));
    }
}
