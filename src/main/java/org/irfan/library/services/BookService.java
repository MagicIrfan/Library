package org.irfan.library.services;

import jakarta.persistence.EntityNotFoundException;
import org.irfan.library.Model.Author;
import org.irfan.library.Model.Book;
import org.irfan.library.Model.Type;
import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dao.BookRepository;
import org.irfan.library.dao.BookTypeRepository;
import org.irfan.library.dto.AuthorDTO;
import org.irfan.library.dto.BookDTO;
import org.irfan.library.dto.BookTypeDTO;
import org.irfan.library.dto.request.CreateBookRequest;
import org.irfan.library.dto.request.EditBookRequest;
import org.irfan.library.exception.DuplicateDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookTypeRepository bookTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, ModelMapper modelMapper, BookTypeRepository bookTypeRepository){
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
        this.bookTypeRepository = bookTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getBookByCriterias(Optional<Long> id, Optional<String> title, Optional<Long> authorId, Optional<Long> bookTypeId){
        return bookRepository.findBooksCustom(id,title,authorId,bookTypeId)
                .stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList();
    }

    @Transactional
    public void createBook(CreateBookRequest request) {
        Author author = authorRepository.findById(request.getAuthor_id())
                .orElseThrow(() -> new EntityNotFoundException("Auteur non trouvé avec l'ID: " + request.getAuthor_id()));
        Type bookType = bookTypeRepository.findById(request.getBooktype_id())
                .orElseThrow(() -> new EntityNotFoundException("Type de livre non trouvé avec l'ID: " + request.getBooktype_id()));
        if (bookRepository.existsByTitle(request.getTitle())) {
            throw new DuplicateDataException("Ce livre existe déjà !");
        }
        bookRepository.save(new Book(request.getTitle(), author, bookType));
    }

    @Transactional
    public void editBook(Integer book_id, EditBookRequest request){
        Book book = bookRepository.findById(book_id)
                .orElseThrow(() -> new EntityNotFoundException("Aucun livre trouvé avec l'ID: " + book_id));

        Optional.ofNullable(request.getAuthor_id()).ifPresent(authorId -> {
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException("Auteur non trouvé avec l'ID: " + authorId));
            if (!author.equals(book.getAuthor())) {
                book.setAuthor(author);
            }
        });

        Optional.ofNullable(request.getBooktype_id()).ifPresent(bookTypeId -> {
            Type type = bookTypeRepository.findById(bookTypeId)
                    .orElseThrow(() -> new EntityNotFoundException("Type de livre non trouvé avec l'ID: " + bookTypeId));
            if (!type.equals(book.getType())) {
                book.setType(type);
            }
        });

        Optional.ofNullable(request.getTitle())
                .filter(StringUtils::hasText)
                .ifPresent(book::setTitle);

        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Integer bookId){
        boolean exists = bookRepository.existsById(bookId);
        if (!exists) {
            throw new EntityNotFoundException("Aucun livre trouvé avec l'ID: " + bookId);
        }
        bookRepository.deleteById(bookId);
    }
}
