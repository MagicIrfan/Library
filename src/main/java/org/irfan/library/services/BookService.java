package org.irfan.library.services;

import jakarta.persistence.EntityNotFoundException;
import org.irfan.library.model.Author;
import org.irfan.library.model.Book;
import org.irfan.library.model.BookType;
import org.irfan.library.dao.*;
import org.irfan.library.dto.*;
import org.irfan.library.dto.request.CreateBookRequest;
import org.irfan.library.dto.request.EditBookRequest;
import org.irfan.library.exception.DuplicateDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorServiceImpl authorService;
    private final BookTypeService bookTypeService;
    private final ModelMapper modelMapper;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorServiceImpl authorService, ModelMapper modelMapper, BookTypeService bookTypeService){
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.modelMapper = modelMapper;
        this.bookTypeService = bookTypeService;
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getBookByCriterias(Optional<Integer> id, Optional<String> title, Optional<Integer> authorId, Optional<Integer> bookTypeId){
        return bookRepository.findBooksCustom(id,title,authorId,bookTypeId)
                .stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public BookDTO getBookById(Integer id){
        return bookRepository.findById(id)
                .map(book -> modelMapper.map(book,BookDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Livre non trouvé avec l'id " + id));
    }

    @Transactional(readOnly = true)
    public List<BookWithoutAuthorDTO> getBooksByAuthorId(Integer authorId){
        return bookRepository.findAllByAuthor_Id(authorId)
                .stream()
                .map(book -> modelMapper.map(book, BookWithoutAuthorDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean existsByTitle(String title){
        return bookRepository.existsByTitle(title);
    }

    @Transactional(readOnly = true)
    public Book getBookEntityById(Integer id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aucun livre trouvé avec l'ID: " + id));
    }

    @Transactional
    public BookDTO createBook(CreateBookRequest request) {
        Author author = authorService.getAuthorEntityById(request.getAuthorId());
        BookType bookType = bookTypeService.getBookTypeEntityById(request.getBooktypeId());
        if (existsByTitle(request.getTitle())) {
            throw new DuplicateDataException("Ce livre existe déjà !");
        }
        Book newBook = bookRepository.save(new Book(request.getTitle(), author, bookType));
        return modelMapper.map(newBook, BookDTO.class);
    }

    @Transactional
    public BookDTO editBook(Integer bookId, EditBookRequest request){
        Book book = getBookEntityById(bookId);

        Optional.ofNullable(request.getAuthorId()).ifPresent(authorId -> {
            Author author = authorService.getAuthorEntityById(authorId);
            book.setAuthor(author);
        });

        Optional.ofNullable(request.getBooktypeId()).ifPresent(bookTypeId -> {
            BookType bookType = bookTypeService.getBookTypeEntityById(bookTypeId);
            book.setBookType(bookType);
        });

        Optional.ofNullable(request.getTitle())
                .filter(StringUtils::hasText)
                .ifPresent(book::setTitle);

        Book editedBook = bookRepository.save(book);
        return modelMapper.map(editedBook,BookDTO.class);
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
