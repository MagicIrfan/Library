package org.irfan.library.controllers;

import jakarta.validation.Valid;
import org.irfan.library.dto.*;
import org.irfan.library.dto.request.CreateBookTypeRequest;
import org.irfan.library.dto.response.ErrorMessageResponse;
import org.irfan.library.dto.response.OKMessageResponse;
import org.irfan.library.services.BookTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/booktype")
@CrossOrigin
public class BookTypeController {
    private final BookTypeService bookTypeService;

    @Autowired
    public BookTypeController(BookTypeService bookTypeService){
        this.bookTypeService = bookTypeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookTypeDTO>> getAllBookTypes() {
        List<BookTypeDTO> bookTypes = bookTypeService.getAllBookTypes();
        return !bookTypes.isEmpty() ? ResponseEntity.ok().body(bookTypes) : ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<?> getBookTypeByName(@RequestParam(value = "name", required = false) Optional<String> name) {
        boolean nameIsPresent = name.isPresent() && !name.get().isEmpty();
        if (nameIsPresent) {
            return bookTypeService.getBookTypeByName(name.get())
                    .map(bookType -> ResponseEntity.ok().body(bookType))
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.badRequest().body(new ErrorMessageResponse<>("Please provide at least a book type name."));
    }

    @PostMapping()
    public ResponseEntity<?> createBookType(@Valid @RequestBody CreateBookTypeRequest request){
        bookTypeService.createBookType(request.getName());
        return ResponseEntity.ok(new OKMessageResponse<>("Le type de livre " + request.getName() + " a été crée"));
    }
}
