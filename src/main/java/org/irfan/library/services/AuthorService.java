package org.irfan.library.services;

import jakarta.persistence.EntityNotFoundException;
import org.irfan.library.Model.Author;
import org.irfan.library.Model.Book;
import org.irfan.library.Model.Type;
import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dao.BookRepository;
import org.irfan.library.dao.BookTypeRepository;
import org.irfan.library.dto.AuthorWithBooksDTO;
import org.irfan.library.dto.BookWithoutAuthorDTO;
import org.irfan.library.dto.request.AddBookToAuthorRequest;
import org.irfan.library.dto.request.CreateAuthorRequest;
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
    private final BookRepository bookRepository;
    private final BookTypeRepository bookTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthorService(AuthorRepository authorRepository,
                         BookRepository bookRepository,
                         BookTypeRepository bookTypeRepository,
                         ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookTypeRepository = bookTypeRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<AuthorWithBooksDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(author -> modelMapper.map(author,AuthorWithBooksDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<AuthorWithBooksDTO> getAuthorByFirstName(String name) {
        return authorRepository.findByFirstname(name)
                .map(author -> modelMapper.map(author,AuthorWithBooksDTO.class));
    }

    @Transactional(readOnly = true)
    public Optional<AuthorWithBooksDTO> getAuthorByFirstNameAndLastName(String firstname, String lastname) {
        return authorRepository.findByFirstnameAndLastname(firstname,lastname)
                .map(author -> modelMapper.map(author,AuthorWithBooksDTO.class));
    }

    @Transactional(readOnly = true)
    public Optional<AuthorWithBooksDTO> getAuthorByLastName(String lastname) {
        return authorRepository.findByLastname(lastname)
                .map(author -> modelMapper.map(author,AuthorWithBooksDTO.class));
    }

    @Transactional
    public void createAuthor(CreateAuthorRequest request){
        if(authorRepository.existsByFirstnameAndLastname(request.getFirstname(), request.getLastname())){
            throw new DuplicateDataException("Vous ne pouvez pas ajouter cet auteur, car il existe déjà");
        }
        authorRepository.save(new Author(request.getFirstname(),request.getLastname()));
    }

    @Transactional
    public void editAuthor(Integer id, EditAuthorRequest request){
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pas trouvé"));
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
            throw new EntityNotFoundException("Trouvé");
        }
        authorRepository.deleteById(id);
    }

    @Transactional
    public void addBookToAuthor(Integer id, AddBookToAuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pas trouvé"));
        Type bookType = bookTypeRepository.findById(request.getBooktype_id())
                .orElseThrow(() -> new EntityNotFoundException("Type de livre non trouvé avec l'ID: " + request.getBooktype_id()));
        if (bookRepository.existsByTitle(request.getTitle())) {
            throw new DuplicateDataException("Ce livre existe déjà !");
        }
        bookRepository.save(new Book(request.getTitle(),author,bookType));
    }

    public List<BookWithoutAuthorDTO> getBooksOfAuthor(Integer id) {
        return bookRepository.findAllByAuthor_Id(Long.valueOf(id))
                .stream()
                .map(book -> modelMapper.map(book, BookWithoutAuthorDTO.class))
                .toList();
    }
}
