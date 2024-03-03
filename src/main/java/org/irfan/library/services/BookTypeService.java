package org.irfan.library.services;

import org.irfan.library.Model.Type;
import org.irfan.library.dao.BookTypeRepository;
import org.irfan.library.dto.BookTypeDTO;
import org.irfan.library.exception.DuplicateDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookTypeService {
    private final BookTypeRepository bookTypeRepository;

    @Autowired
    public BookTypeService(BookTypeRepository bookTypeRepository){
        this.bookTypeRepository = bookTypeRepository;
    }

    @Transactional(readOnly = true)
    public Optional<BookTypeDTO> getBookTypeByName(String name){
        return bookTypeRepository.findByName(name)
                .map(bookType -> new BookTypeDTO(bookType.getName()));
    }

    @Transactional(readOnly = true)
    public List<BookTypeDTO> getAllBookTypes(){
        return bookTypeRepository.findAll()
                .stream()
                .map(bookType -> new BookTypeDTO(bookType.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createBookType(String name){
        if(bookTypeRepository.existsByName(name)){
            throw new DuplicateDataException("Vous ne pouvez pas ajouter ce type de livre, car il existe déjà");
        }
        bookTypeRepository.save(new Type(name));
    }
}
