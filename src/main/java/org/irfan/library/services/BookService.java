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
import org.irfan.library.exception.DuplicateDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<BookDTO> getAllByAuthor(Integer author_id){
        Optional<Author> authorOptional = authorRepository.findById(author_id);
        if(authorOptional.isPresent()){
            Author author = authorOptional.get();
            AuthorDTO authorDTO = modelMapper.map(author,AuthorDTO.class);
            return bookRepository.findAllByAuthor(author)
                    .stream()
                    .map(book -> new BookDTO(book.getTitle(),authorDTO,new BookTypeDTO(book.getType().getName())))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getAllByBookType(Integer booktype_id){
        Optional<Type> typeOptional = bookTypeRepository.findById(booktype_id);
        if(typeOptional.isPresent()){
            Type type = typeOptional.get();
            BookTypeDTO bookTypeDTO = modelMapper.map(type,BookTypeDTO.class);
            return bookRepository.findAllByType(type)
                    .stream()
                    .map(book -> modelMapper.map(book,BookDTO.class))
                    .toList();
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public Optional<BookDTO> getBookByTitle(String title){
        return bookRepository.findByTitle(title)
                .map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks(){
        return bookRepository.findAll()
                .stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<BookDTO> getBookById(Long id){
        return bookRepository.findById(Math.toIntExact(id))
                .map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Transactional(readOnly = true)
    public Optional<BookDTO> getBookByIdAndTitle(Long id, String title){
        return bookRepository.findByIdAndTitle(id,title)
                .map(book -> modelMapper.map(book, BookDTO.class));
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
}
