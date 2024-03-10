package org.irfan.library.services;

import jakarta.persistence.EntityNotFoundException;
import org.irfan.library.Model.Type;
import org.irfan.library.dao.BookTypeRepository;
import org.irfan.library.dto.BookTypeDTO;
import org.irfan.library.exception.DuplicateDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookTypeService {
    private final BookTypeRepository bookTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookTypeService(BookTypeRepository bookTypeRepository, ModelMapper modelMapper){
        this.bookTypeRepository = bookTypeRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public Optional<BookTypeDTO> getBookTypeByName(String name){
        return bookTypeRepository.findByName(name)
                .map(bookType -> new BookTypeDTO(bookType.getName()));
    }

    @Transactional(readOnly = true)
    public List<BookTypeDTO> getAllBookTypes(int page, int size){
        Pageable paging = PageRequest.of(page, size);
        Page<Type> pagedResult = bookTypeRepository.findAll(paging); // Use paging here

        if (pagedResult.hasContent()) {
            return pagedResult.getContent()
                    .stream()
                    .map(bookType -> new BookTypeDTO(bookType.getName()))
                    .toList();
        } else {
            return new ArrayList<BookTypeDTO>();
        }
    }

    @Transactional(readOnly = true)
    public BookTypeDTO getBookTypeById(Integer id){
        return bookTypeRepository.findById(id)
                .map(bookType -> modelMapper.map(bookType, BookTypeDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("BookType with ID " + id + " not found"));
    }

    @Transactional
    public void createBookType(String name){
        if(bookTypeRepository.existsByName(name)){
            throw new DuplicateDataException("Vous ne pouvez pas ajouter ce type de livre, car il existe déjà");
        }
        bookTypeRepository.save(new Type(name));
    }
}
