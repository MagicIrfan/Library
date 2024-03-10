package org.irfan.library.services;

import jakarta.persistence.EntityNotFoundException;
import org.irfan.library.Model.Author;
import org.irfan.library.Model.Book;
import org.irfan.library.Model.Type;
import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dao.BookRepository;
import org.irfan.library.dao.BookTypeRepository;
import org.irfan.library.dto.AuthorDTO;
import org.irfan.library.dto.AuthorWithBooksDTO;
import org.irfan.library.dto.BookTypeDTO;
import org.irfan.library.dto.BookWithoutAuthorDTO;
import org.irfan.library.dto.request.AddBookToAuthorRequest;
import org.irfan.library.dto.request.CreateAuthorRequest;
import org.irfan.library.dto.request.CreateBookRequest;
import org.irfan.library.dto.request.EditAuthorRequest;
import org.irfan.library.exception.DuplicateDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthorService(AuthorRepository authorRepository,
                         ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<AuthorWithBooksDTO> getAuthorsByCriteria(Optional<Long> id, Optional<String> firstname, Optional<String> lastname, Optional<Long> bookId) {
        return authorRepository.findAuthorsCustom(id,firstname,lastname,bookId).stream()
                .map(author -> modelMapper.map(author,AuthorWithBooksDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public AuthorDTO getAuthorById(Integer id){
        return authorRepository.findById(id)
                .map(author -> modelMapper.map(author,AuthorDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Auteur non trouvé avec l'id " + id));
    }

    @Transactional
    public AuthorDTO createAuthor(CreateAuthorRequest request){
        if(authorRepository.existsByFirstnameAndLastname(request.getFirstname(), request.getLastname())){
            throw new DuplicateDataException("Vous ne pouvez pas ajouter cet auteur, car il existe déjà");
        }
        Author author = authorRepository.save(new Author(request.getFirstname(),request.getLastname()));
        return modelMapper.map(author,AuthorDTO.class);
    }

    @Transactional
    public void editAuthor(Integer id, EditAuthorRequest request){
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("L'auteur n'existe pas avec l'id " + id));
        Optional.ofNullable(request.getFirstname())
                .filter(StringUtils::hasText)
                .ifPresent(author::setFirstname);
        Optional.ofNullable(request.getLastname())
                .filter(StringUtils::hasText)
                .ifPresent(author::setLastname);
        authorRepository.save(author);
    }

    @Transactional
    public void deleteAuthor(Integer id){
        boolean authorExists = authorRepository.existsById(id);
        if(!authorExists){
            throw new EntityNotFoundException("L'auteur n'existe pas avec l'id " + id);
        }
        authorRepository.deleteById(id);
    }
}
