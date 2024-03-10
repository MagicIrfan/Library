package org.irfan.library.controllers;

import jakarta.validation.Valid;
import org.irfan.library.dto.*;
import org.irfan.library.dto.request.CreateBookTypeRequest;
import org.irfan.library.dto.request.EditBookRequest;
import org.irfan.library.dto.response.ErrorMessageResponse;
import org.irfan.library.dto.response.OKMessageResponse;
import org.irfan.library.services.BookTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/booktypes")
@CrossOrigin
public class BookTypeController {
    private final BookTypeService bookTypeService;

    @Autowired
    public BookTypeController(BookTypeService bookTypeService){
        this.bookTypeService = bookTypeService;
    }

    @GetMapping
    public ResponseEntity<List<BookTypeDTO>> getBookTypes() {
        List<BookTypeDTO> bookTypes = bookTypeService.getAllBookTypes();
        return !bookTypes.isEmpty() ? ResponseEntity.ok().body(bookTypes) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookTypeDTO> getBookTypeById(@PathVariable Integer id) {
        BookTypeDTO bookType = bookTypeService.getBookTypeById(id);
        return ResponseEntity.ok(bookType);
    }

    @PostMapping()
    public ResponseEntity<BookTypeDTO> createBookType(@Valid @RequestBody CreateBookTypeRequest request){
        return ResponseEntity.ok(bookTypeService.createBookType(request.getName()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookTypeDTO> editBookType(@PathVariable Integer id, @RequestBody String name){
        return ResponseEntity.ok(bookTypeService.editBookType(id,name));
    }
}
