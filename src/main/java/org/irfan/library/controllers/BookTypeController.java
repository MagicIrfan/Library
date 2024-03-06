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
@RequestMapping("/api/v1/booktypes")
@CrossOrigin
public class BookTypeController {
    private final BookTypeService bookTypeService;

    @Autowired
    public BookTypeController(BookTypeService bookTypeService){
        this.bookTypeService = bookTypeService;
    }

    @GetMapping
    public ResponseEntity<?> getBookTypes(@RequestParam(value = "name", required = false) Optional<String> name,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "3") int size) {
        if (name.isPresent() && !name.get().isEmpty()) {
            return bookTypeService.getBookTypeByName(name.get())
                    .map(bookType -> ResponseEntity.ok().body(bookType))
                    .orElse(ResponseEntity.notFound().build());
        } else {
            List<BookTypeDTO> bookTypes = bookTypeService.getAllBookTypes(page, size);
            return !bookTypes.isEmpty() ? ResponseEntity.ok().body(bookTypes) : ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<?> createBookType(@Valid @RequestBody CreateBookTypeRequest request){
        bookTypeService.createBookType(request.getName());
        return ResponseEntity.ok(new OKMessageResponse<>("Le type de livre " + request.getName() + " a été crée"));
    }
}
