package org.irfan.library;

import jakarta.persistence.EntityNotFoundException;
import org.irfan.library.model.Author;
import org.irfan.library.model.Book;
import org.irfan.library.model.BookType;
import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dao.BookRepository;
import org.irfan.library.dao.BookTypeRepository;
import org.irfan.library.dto.BookDTO;
import org.irfan.library.dto.request.CreateBookRequest;
import org.irfan.library.dto.request.EditBookRequest;
import org.irfan.library.services.AuthorServiceImpl;
import org.irfan.library.services.BookService;
import org.irfan.library.services.BookTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookTypeRepository bookTypeRepository;
    private ModelMapper modelMapper;
    private BookService bookService;
    private AuthorServiceImpl authorService;
    private BookTypeService bookTypeService;

    @BeforeEach
    void setUp() {
        // Initialisation des services
        modelMapper = new ModelMapper();
        authorService = new AuthorServiceImpl(authorRepository,modelMapper);
        bookTypeService = new BookTypeService(bookTypeRepository,modelMapper);
        bookService = new BookService(bookRepository, authorService, modelMapper, bookTypeService);
    }

    @Test
    void whenFindBooksByAuthor_thenReturnBookList() {
        // Given
        BookType bookType = new BookType("Roman");
        Author author = new Author(1,"Victor", "Hugo", new ArrayList<>());
        Book book1 = new Book("Les Misérables", author, bookType);
        Book book2 = new Book("Le Dernier Jour d'un Condamné", author, bookType);
        List<Book> bookList = Arrays.asList(book1, book2);

        when(bookRepository.findBooksCustom(Optional.empty(),Optional.empty(), Optional.ofNullable(author.getId()),Optional.empty())).thenReturn(bookList);

        // When
        List<BookDTO> result = bookService.getBookByCriterias(Optional.empty(),Optional.empty(),Optional.of(author.getId()),Optional.empty());

        // Then
        verify(bookRepository).findBooksCustom(Optional.empty(), Optional.empty(), Optional.of(author.getId()), Optional.empty());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void whenFindBooksById_thenReturnBook() {
        // Given
        BookType bookType = new BookType("Roman");
        Author author = new Author(1,"Victor", "Hugo", new ArrayList<>());
        Book book1 = new Book(1,"Les Misérables", author, bookType);

        when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));

        // When
        BookDTO result = bookService.getBookById(book1.getId());

        // Then
        verify(bookRepository).findById(book1.getId());
        assertNotNull(result);
        assertEquals("Les Misérables",result.getTitle());
    }

    @Test
    void whenCreateBook_thenBookIsCreated() {
        // Given
        BookType bookType = new BookType(1,"Roman");
        Author author = new Author(1,"Victor", "Hugo", new ArrayList<>());
        Book book1 = new Book(1,"Les Misérables", author, bookType);

        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(bookTypeRepository.findById(bookType.getId())).thenReturn(Optional.of(bookType));
        when(bookRepository.existsByTitle(book1.getTitle())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenAnswer(a -> a.getArguments()[0]);

        //When
        bookService.createBook(new CreateBookRequest(author.getId(), bookType.getId(),book1.getTitle()));

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        Book savedBook = bookArgumentCaptor.getValue();

        assertNotNull(savedBook);
        assertEquals(book1.getTitle(), savedBook.getTitle());
        assertEquals(author.getId(), savedBook.getAuthor().getId());
        assertEquals(bookType.getId(), savedBook.getBookType().getId());
    }

    @Test
    void whenEditBookType_thenBookTypeIsUpdated() {
        // Given
        EditBookRequest request = new EditBookRequest();
        BookType bookType = new BookType(1,"Roman");
        BookType bookType2 = new BookType(2,"Manga");
        request.setBooktypeId(bookType2.getId());
        Author author = new Author(1,"Victor", "Hugo", new ArrayList<>());
        Book book1 = new Book(1,"Les Misérables", author, bookType);

        when(bookTypeRepository.findById(bookType2.getId())).thenReturn(Optional.of(bookType2));
        when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        //When
        bookService.editBook(book1.getId(),request);

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        Book savedBook = bookArgumentCaptor.getValue();

        assertNotNull(savedBook);
        assertEquals(bookType2, savedBook.getBookType());
        assertEquals(author, savedBook.getAuthor());
        assertEquals(book1.getTitle(), savedBook.getTitle());
    }

    @Test
    void whenEditBook_thenBookIsUpdated() {
        // Given
        String newTitle = "One Piece";
        BookType bookType = new BookType(1,"Roman");
        BookType bookType2 = new BookType(2,"Manga");
        Author author = new Author(1,"Victor", "Hugo", new ArrayList<>());
        Author author2 = new Author(2,"Irfan", "BOUHENAF", new ArrayList<>());
        Book book1 = new Book(1,"Les Misérables", author, bookType);
        EditBookRequest request = EditBookRequest.builder()
                .title(newTitle)
                .booktypeId(bookType2.getId())
                .authorId(author2.getId())
                .build();

        when(authorRepository.findById(author2.getId())).thenReturn(Optional.of(author2));
        when(bookTypeRepository.findById(bookType2.getId())).thenReturn(Optional.of(bookType2));
        when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        bookService.editBook(book1.getId(),request);

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        Book savedBook = bookArgumentCaptor.getValue();

        assertNotNull(savedBook);
        assertEquals(newTitle, savedBook.getTitle());
        assertEquals(bookType2, savedBook.getBookType());
        assertEquals(author2, savedBook.getAuthor());
    }

    @Test
    void whenDeleteBook_thenBookIsDeleted(){
        // Given
        Integer bookId = 1; // Use the ID directly for clarity
        when(bookRepository.existsById(bookId)).thenReturn(true);

        // When
        bookService.deleteBook(bookId);

        // Then
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void whenEditBookAndBookNotFound_thenThrowsException() {
        // Given
        int nonExistentBookId = 999;
        EditBookRequest request = new EditBookRequest(); // configurez votre requête selon les besoins

        when(bookRepository.findById(nonExistentBookId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            bookService.editBook(nonExistentBookId, request);
        });
    }

    @Test
    void whenDeleteBookAndBookNotFound_thenThrowsException(){
        // Given
        int nonExistentBookId = 999;

        when(bookRepository.existsById(nonExistentBookId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            bookService.deleteBook(nonExistentBookId);
        });
    }
}
