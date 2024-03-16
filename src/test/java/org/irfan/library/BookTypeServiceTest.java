package org.irfan.library;

import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dao.BookRepository;
import org.irfan.library.dao.BookTypeRepository;
import org.irfan.library.services.AuthorService;
import org.irfan.library.services.BookService;
import org.irfan.library.services.BookTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

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
}
