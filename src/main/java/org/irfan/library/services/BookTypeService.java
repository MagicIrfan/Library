package org.irfan.library.services;

import jakarta.persistence.EntityNotFoundException;
import org.irfan.library.Model.BookType;
import org.irfan.library.dao.BookTypeRepository;
import org.irfan.library.dto.BookTypeDTO;
import org.irfan.library.exception.DuplicateDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        BookType bookType = getBookTypeEntityById(id);
        return modelMapper.map(bookType,BookTypeDTO.class);
    }

    @Transactional(readOnly = true)
    public BookType getBookTypeEntityById(Integer id){
        return bookTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BookType with ID " + id + " not found"));
    }

    @Transactional
    public BookTypeDTO createBookType(String name){
        if(bookTypeRepository.existsByName(name)){
            throw new DuplicateDataException("Vous ne pouvez pas ajouter ce type de livre, car il existe déjà");
        }
        BookType newBookBookType = bookTypeRepository.save(new BookType(name));
        return modelMapper.map(newBookBookType,BookTypeDTO.class);
    }

    @Transactional
    public BookTypeDTO editBookType(Integer id, String name){
        BookType oldBookBookType = getBookTypeEntityById(id);
        oldBookBookType.setName(name);
        BookType newBookBookType = bookTypeRepository.save(oldBookBookType);
        return modelMapper.map(newBookBookType,BookTypeDTO.class);
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
