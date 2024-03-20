package org.irfan.library;

import jakarta.persistence.EntityNotFoundException;
import org.irfan.library.model.BookType;
import org.irfan.library.dao.BookTypeRepository;
import org.irfan.library.services.BookTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookTypeServiceTest {
    @Mock
    private BookTypeRepository bookTypeRepository;
    private ModelMapper modelMapper;
    private BookTypeService bookTypeService;

    @BeforeEach
    public void setUp() {
        // Initialisation des services
        modelMapper = new ModelMapper();
        bookTypeService = new BookTypeService(bookTypeRepository,modelMapper);
    }

    @Test
    public void whenCreateBookType_thenBookTypeIsCreated()
    {
        //Given
        String name = "Roman";
        when(bookTypeRepository.existsByName(name)).thenReturn(false);
        when(bookTypeRepository.save(any(BookType.class))).thenAnswer(a -> a.getArguments()[0]);

        //When
        bookTypeService.createBookType("Roman");

        //Then
        ArgumentCaptor<BookType> bookTypeArgumentCaptor = ArgumentCaptor.forClass(BookType.class);
        verify(bookTypeRepository).save(bookTypeArgumentCaptor.capture());
        BookType savedBookType = bookTypeArgumentCaptor.getValue();

        assertNotNull(savedBookType);
        assertEquals(name,savedBookType.getName());
    }

    @Test
    public void whenEditBookType_thenBookTypeIsUpdated()
    {
        //Given
        String oldName = "Roman";
        String newName = "Manga";
        BookType bookType = new BookType(1,oldName);
        when(bookTypeRepository.findById(1)).thenReturn(Optional.of(bookType));
        when(bookTypeRepository.save(any(BookType.class))).thenAnswer(a -> a.getArguments()[0]);

        //When
        bookTypeService.editBookType(1,newName);

        //Then
        ArgumentCaptor<BookType> bookTypeArgumentCaptor = ArgumentCaptor.forClass(BookType.class);
        verify(bookTypeRepository).save(bookTypeArgumentCaptor.capture());
        BookType savedBookType = bookTypeArgumentCaptor.getValue();

        assertNotNull(savedBookType);
        assertEquals(newName,savedBookType.getName());
    }

    @Test
    public void whenDeleteAuthor_thenAuthorIsDeleted(){
        // Given
        Integer bookTypeId = 1; // Use the ID directly for clarity
        when(bookTypeRepository.existsById(bookTypeId)).thenReturn(true);

        // When
        bookTypeService.deleteBookType(bookTypeId);

        // Then
        verify(bookTypeRepository).deleteById(bookTypeId);
    }

    @Test
    public void whenEditBookAndBookNotFound_thenThrowsException() {
        // Given
        int nonExistentId = 999;

        when(bookTypeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            bookTypeService.editBookType(nonExistentId, "Test");
        });
    }

    @Test
    public void whenDeleteBookAndBookNotFound_thenThrowsException(){
        // Given
        int nonExistentId = 999;

        when(bookTypeRepository.existsById(nonExistentId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            bookTypeService.deleteBookType(nonExistentId);
        });
    }
}
