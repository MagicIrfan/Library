package org.irfan.library.unit;

import jakarta.persistence.EntityNotFoundException;
import org.irfan.library.Model.Author;
import org.irfan.library.Model.Book;
import org.irfan.library.Model.Type;
import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dao.BookRepository;
import org.irfan.library.dao.BookTypeRepository;
import org.irfan.library.dto.BookDTO;
import org.irfan.library.dto.request.CreateBookRequest;
import org.irfan.library.dto.request.EditBookRequest;
import org.irfan.library.services.AuthorService;
import org.irfan.library.services.BookService;
import org.irfan.library.services.BookTypeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    private ModelMapper modelMapper;
    private BookService bookService;
    private AuthorService authorService;
    private BookTypeService bookTypeService;

    @BeforeEach
    public void setUp() {
        // Initialisation des services
        modelMapper = new ModelMapper();
        authorService = new AuthorService(authorRepository,modelMapper);
        bookTypeService = new BookTypeService(bookTypeRepository,modelMapper);
        bookService = new BookService(bookRepository, authorService, modelMapper, bookTypeService);
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
    public void whenCreateBook_thenBookIsCreated() {
        // Given
        Type type = new Type(1,"Roman");
        Author author = new Author(1L,"Victor", "Hugo", new ArrayList<>());
        Book book1 = new Book(1L,"Les Misérables", author,type);

        when(authorRepository.findById(Math.toIntExact(author.getId()))).thenReturn(Optional.of(author));
        when(bookTypeRepository.findById(type.getId())).thenReturn(Optional.of(type));
        when(bookRepository.existsByTitle(book1.getTitle())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenAnswer(a -> a.getArguments()[0]);

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

    @Test
    public void whenEditBookType_thenBookTypeIsUpdated() {
        // Given
        EditBookRequest request = new EditBookRequest();
        Type type = new Type(1,"Roman");
        Type type2 = new Type(2,"Manga");
        request.setBooktype_id(type2.getId());
        Author author = new Author(1L,"Victor", "Hugo", new ArrayList<>());
        Book book1 = new Book(1L,"Les Misérables", author,type);

        when(bookTypeRepository.findById(type2.getId())).thenReturn(Optional.of(type2));
        when(bookRepository.findById(Math.toIntExact(book1.getId()))).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        //When
        bookService.editBook(Math.toIntExact(book1.getId()),request);

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        Book savedBook = bookArgumentCaptor.getValue();

        assertNotNull(savedBook);
        assertEquals(type2, savedBook.getType());
        assertEquals(author, savedBook.getAuthor());
        assertEquals(book1.getTitle(), savedBook.getTitle());
    }

    @Test
    public void whenEditBook_thenBookIsUpdated() {
        // Given
        String newTitle = "One Piece";
        Type type = new Type(1,"Roman");
        Type type2 = new Type(2,"Manga");
        Author author = new Author(1L,"Victor", "Hugo", new ArrayList<>());
        Author author2 = new Author(2L,"Irfan", "BOUHENAF", new ArrayList<>());
        Book book1 = new Book(1L,"Les Misérables", author,type);
        EditBookRequest request = EditBookRequest.builder()
                .title(newTitle)
                .booktype_id(type2.getId())
                .author_id(Math.toIntExact(author2.getId()))
                .build();

        when(authorRepository.findById(Math.toIntExact(author2.getId()))).thenReturn(Optional.of(author2));
        when(bookTypeRepository.findById(type2.getId())).thenReturn(Optional.of(type2));
        when(bookRepository.findById(Math.toIntExact(book1.getId()))).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        bookService.editBook(Math.toIntExact(book1.getId()),request);

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        Book savedBook = bookArgumentCaptor.getValue();

        assertNotNull(savedBook);
        assertEquals(newTitle, savedBook.getTitle());
        assertEquals(type2, savedBook.getType());
        assertEquals(author2, savedBook.getAuthor());
    }

    @Test
    public void whenDeleteBook_thenBookIsDeleted(){
        // Given
        long bookId = 1L; // Use the ID directly for clarity
        when(bookRepository.existsById((int) bookId)).thenReturn(true);

        // When
        bookService.deleteBook((int) bookId);

        // Then
        verify(bookRepository).deleteById((int) bookId);
    }

    @Test
    public void whenEditBookAndBookNotFound_thenThrowsException() {
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
    public void whenDeleteBookAndBookNotFound_thenThrowsException(){
        // Given
        int nonExistentBookId = 999;

        when(bookRepository.existsById(nonExistentBookId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            bookService.deleteBook(nonExistentBookId);
        });
    }
}
