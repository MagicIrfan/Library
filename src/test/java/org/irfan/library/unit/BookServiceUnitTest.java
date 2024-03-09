package org.irfan.library.unit;

import org.irfan.library.Model.Author;
import org.irfan.library.Model.Book;
import org.irfan.library.Model.Type;
import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dao.BookRepository;
import org.irfan.library.dao.BookTypeRepository;
import org.irfan.library.dto.BookDTO;
import org.irfan.library.dto.request.CreateBookRequest;
import org.irfan.library.services.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookTypeRepository bookTypeRepository;
    private final ModelMapper modelMapper = new ModelMapper(); // Initialisation directe
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        // Initialisation de BookService avec les mocks et modelMapper
        bookService = new BookService(bookRepository, authorRepository, modelMapper, bookTypeRepository);
    }

    @Test
    public void whenFindBooksByAuthor_thenReturnBookList() {
        // Given
        Type type = new Type("Roman");
        Author author = new Author(1L,"Victor", "Hugo", new ArrayList<>());
        Book book1 = new Book("Les Misérables", author,type);
        Book book2 = new Book("Le Dernier Jour d'un Condamné", author,type);
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
    public void whenFindBooksById_thenReturnBook() {
        // Given
        Type type = new Type("Roman");
        Author author = new Author(1L,"Victor", "Hugo", new ArrayList<>());
        Book book1 = new Book(1L,"Les Misérables", author,type);

        when(bookRepository.findById(Math.toIntExact(book1.getId()))).thenReturn(Optional.of(book1));

        // When
        BookDTO result = bookService.getBookById(Math.toIntExact(book1.getId()));

        // Then
        verify(bookRepository).findById(Math.toIntExact(book1.getId()));
        assertNotNull(result);
        assertEquals("Les Misérables",result.getTitle());
    }

    @Test
    public void whenCreateBook_thenCreateBook() {
        // Given
        Type type = new Type("Roman");
        Author author = new Author(1L,"Victor", "Hugo", new ArrayList<>());
        Book book1 = new Book(1L,"Les Misérables", author,type);

        when(authorRepository.findById(Math.toIntExact(author.getId()))).thenReturn(Optional.of(author));
        when(bookTypeRepository.findById(type.getId())).thenReturn(Optional.of(type));
        when(bookRepository.existsByTitle(book1.getTitle())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        //When
        bookService.createBook(new CreateBookRequest(Math.toIntExact(author.getId()),type.getId(),book1.getTitle()));

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        Book savedBook = bookArgumentCaptor.getValue();

        assertNotNull(savedBook);
        assertEquals(book1.getTitle(), savedBook.getTitle());
        assertEquals(author.getId(), savedBook.getAuthor().getId());
        assertEquals(type.getId(), savedBook.getType().getId());
    }
}
