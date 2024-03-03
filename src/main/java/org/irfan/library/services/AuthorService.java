package org.irfan.library.services;

import org.irfan.library.Model.Author;
import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dao.BookRepository;
import org.irfan.library.dto.request.CreateAuthorRequest;
import org.irfan.library.dto.AuthorDTO;
import org.irfan.library.exception.DuplicateDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthorService(AuthorRepository authorRepository,
                         BookRepository bookRepository,
                         ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(author -> modelMapper.map(author,AuthorDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AuthorDTO> getAuthorByFirstName(String name) {
        return authorRepository.findByFirstname(name)
                .map(author -> modelMapper.map(author,AuthorDTO.class));
    }

    @Transactional(readOnly = true)
    public Optional<AuthorDTO> getAuthorByFirstNameAndLastName(String firstname, String lastname) {
        return authorRepository.findByFirstnameAndLastname(firstname,lastname)
                .map(author -> modelMapper.map(author,AuthorDTO.class));
    }

    @Transactional(readOnly = true)
    public Optional<AuthorDTO> getAuthorByLastName(String lastname) {
        return authorRepository.findByLastname(lastname)
                .map(author -> modelMapper.map(author,AuthorDTO.class));
    }

    @Transactional
    public void createAuthor(CreateAuthorRequest request){
        if(authorRepository.existsByFirstnameAndLastname(request.getFirstname(), request.getLastname())){
            throw new DuplicateDataException("Vous ne pouvez pas ajouter cet auteur, car il existe déjà");
        }
        Author author = new Author(request.getFirstname(),request.getLastname());
        authorRepository.save(author);
    }
}
