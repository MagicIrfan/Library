package org.irfan.library.unit;

import jakarta.persistence.EntityNotFoundException;
import org.irfan.library.Model.Author;
import org.irfan.library.Model.Book;
import org.irfan.library.Model.Type;
import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dto.AuthorDTO;
import org.irfan.library.dto.BookDTO;
import org.irfan.library.dto.request.CreateAuthorRequest;
import org.irfan.library.dto.request.CreateBookRequest;
import org.irfan.library.dto.request.EditAuthorRequest;
import org.irfan.library.dto.request.EditBookRequest;
import org.irfan.library.services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceUnitTest {
    @Mock
    private AuthorRepository authorRepository;
    private ModelMapper modelMapper;
    private AuthorService authorService;

    @BeforeEach
    public void setUp() {
        // Initialisation des services
        modelMapper = new ModelMapper();
        authorService = new AuthorService(authorRepository,modelMapper);
    }

    @Test
    public void whenFindAllAuthors_thenReturnList()
    {
        //Given
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Albert", "Camus"));
        authors.add(new Author("Guillaume", "Apollinaire"));
        authors.add(new Author("Victor", "Hugo"));
        authors.add(new Author("Eiichiro", "Oda"));
        when(authorRepository.findAuthorsCustom(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty())).thenReturn(authors);

        // When
        List<AuthorDTO> result = authorService.getAuthorsByCriteria(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty());

        // Then
        verify(authorRepository).findAuthorsCustom(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(4, result.size());
    }

    @Test
    public void whenFindAuthorById_thenReturnAuthor() {
        // Given
        Author author = Author.builder()
                .id(16)
                .firstname("Albert")
                .lastname("Camus")
                .build();
        when(authorRepository.findById(16)).thenReturn(Optional.of(author));

        // When
        Author resultEntity = authorService.getAuthorEntityById(16);
        AuthorDTO resultDTO = authorService.getAuthorById(16);

        // Then
        verify(authorRepository, times(2)).findById(16); // Verification is needed only once
        assertNotNull(resultEntity, "The returned Author entity should not be null");
        assertNotNull(resultDTO, "The returned AuthorDTO should not be null");

        // Asserting the Author entity
        assertEquals(16, resultEntity.getId(), "The ID of the returned Author entity should match");
        assertEquals("Albert", resultEntity.getFirstname(), "The firstname of the returned Author entity should match");
        assertEquals("Camus", resultEntity.getLastname(), "The lastname of the returned Author entity should match");

        // Asserting the AuthorDTO
        assertEquals(16, resultDTO.getId(), "The ID of the returned AuthorDTO should match");
        assertEquals("Albert", resultDTO.getFirstname(), "The firstname of the returned AuthorDTO should match");
        assertEquals("Camus", resultDTO.getLastname(), "The lastname of the returned AuthorDTO should match");
    }

    @Test
    public void whenCreateAuthor_thenAuthorIsCreated() {
        // Given
        Author author = new Author(1,"Victor", "Hugo", new ArrayList<>());

        when(authorRepository.save(any(Author.class))).thenAnswer(a -> a.getArguments()[0]);
        when(authorRepository.existsByFirstnameAndLastname(author.getFirstname(),author.getLastname())).thenReturn(false);

        //When
        authorService.createAuthor(new CreateAuthorRequest(author.getFirstname(),author.getLastname()));

        // Then
        ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(authorArgumentCaptor.capture());
        verify(authorRepository).existsByFirstnameAndLastname(author.getFirstname(),author.getLastname());
        Author savedAuthor = authorArgumentCaptor.getValue();

        assertNotNull(savedAuthor);
        assertEquals("Victor", savedAuthor.getFirstname(), "The first name of the author should match");
        assertEquals("Hugo", savedAuthor.getLastname(), "The last name of the author should match");
    }

    @Test
    public void whenEditAuthor_thenAuthorIsUpdated() {
        String newFirstName = "Eiichiro";
        String newLastName = "Oda";
        Author author = new Author(1,"Victor", "Hugo", new ArrayList<>());

        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenAnswer(a -> a.getArguments()[0]);

        // When
        authorService.editAuthorEntity(author.getId(), new EditAuthorRequest(newFirstName, newLastName));

        // Then
        ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(authorArgumentCaptor.capture());
        Author savedAuthor = authorArgumentCaptor.getValue();

        assertNotNull(savedAuthor);
        assertEquals(newFirstName, savedAuthor.getFirstname(), "The first name of the author should match");
        assertEquals(newLastName, savedAuthor.getLastname(), "The last name of the author should match");
    }

    @Test
    public void whenEditAuthorWithBlanks_thenAuthorIsUpdated() {
        String newFirstName = null;
        String newLastName = null;
        String oldFirstName = "Victor";
        String oldLastName = "Hugo";
        Author author = new Author(1,oldFirstName, oldLastName, new ArrayList<>());

        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenAnswer(a -> a.getArguments()[0]);

        // When
        authorService.editAuthorEntity(author.getId(), new EditAuthorRequest(newFirstName, newLastName));

        // Then
        ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(authorArgumentCaptor.capture());
        Author savedAuthor = authorArgumentCaptor.getValue();

        assertNotNull(savedAuthor);
        assertEquals(oldFirstName, savedAuthor.getFirstname(), "The first name of the author should match");
        assertEquals(oldLastName, savedAuthor.getLastname(), "The last name of the author should match");
    }

    @Test
    public void whenDeleteAuthor_thenAuthorIsDeleted(){
        // Given
        Integer authorId = 1; // Use the ID directly for clarity
        when(authorRepository.existsById(authorId)).thenReturn(true);

        // When
        authorService.deleteAuthor(authorId);

        // Then
        verify(authorRepository).deleteById(authorId);
    }

    @Test
    public void whenEditBookAndBookNotFound_thenThrowsException() {
        // Given
        int nonExistentId = 999;

        when(authorRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            authorService.editAuthorEntity(nonExistentId, new EditAuthorRequest());
        });
    }

    @Test
    public void whenDeleteBookAndBookNotFound_thenThrowsException(){
        // Given
        int nonExistentId = 999;

        when(authorRepository.existsById(nonExistentId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            authorService.deleteAuthor(nonExistentId);
        });
    }
}
