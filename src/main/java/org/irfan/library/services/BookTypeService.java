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
    public List<BookTypeDTO> getAllBookTypes(){
        return bookTypeRepository.findAll()
                    .stream()
                    .map(bookType -> modelMapper.map(bookType,BookTypeDTO.class))
                    .toList();
    }

    @Transactional(readOnly = true)
    public BookTypeDTO getBookTypeById(Integer id){
        Type bookType = getBookTypeEntityById(id);
        return modelMapper.map(bookType,BookTypeDTO.class);
    }

    @Transactional(readOnly = true)
    public Type getBookTypeEntityById(Integer id){
        return bookTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BookType with ID " + id + " not found"));
    }

    @Transactional
    public BookTypeDTO createBookType(String name){
        if(bookTypeRepository.existsByName(name)){
            throw new DuplicateDataException("Vous ne pouvez pas ajouter ce type de livre, car il existe déjà");
        }
        Type newBookType = bookTypeRepository.save(new Type(name));
        return modelMapper.map(newBookType,BookTypeDTO.class);
    }

    @Transactional
    public BookTypeDTO editBookType(Integer id, String name){
        Type oldBookType = getBookTypeEntityById(id);
        oldBookType.setName(name);
        Type newBookType = bookTypeRepository.save(oldBookType);
        return modelMapper.map(newBookType,BookTypeDTO.class);
    }

    @Transactional
    public void deleteBookType(Integer id){
        boolean exists = bookTypeRepository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException("Aucun type de livre trouvé avec l'ID: " + id);
        }
        bookTypeRepository.deleteById(id);
    }
}
