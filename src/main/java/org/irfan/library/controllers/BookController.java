package org.irfan.library.controllers;

import jakarta.validation.Valid;
import org.irfan.library.dto.BookDTO;
import org.irfan.library.dto.request.CreateBookRequest;
import org.irfan.library.dto.request.EditBookRequest;
import org.irfan.library.dto.response.ErrorMessageResponse;
import org.irfan.library.dto.response.OKMessageResponse;
import org.irfan.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping()
    public ResponseEntity<List<BookDTO>> getBooks(@RequestParam(value = "id", required = false) Optional<Integer> id,
                                     @RequestParam(value = "title", required = false) Optional<String> title,
                                     @RequestParam(value = "authorId", required = false) Optional<Integer> authorId,
                                     @RequestParam(value = "bookTypeId", required = false) Optional<Integer> bookTypeId){

        return ResponseEntity.ok().body(bookService.getBookByCriterias(id,title,authorId,bookTypeId));
    }

    @PostMapping()
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody CreateBookRequest request){
        BookDTO newBook = bookService.createBook(request);
        return ResponseEntity.ok(newBook);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OKMessageResponse<String>> editBook(@PathVariable(name = "id") Integer book_id, @Valid @RequestBody EditBookRequest request){
        bookService.editBook(book_id,request);
        return ResponseEntity.ok(new OKMessageResponse<>("Livre modifié avec succès."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OKMessageResponse<String>> deleteBook(@PathVariable(name = "id") Integer book_id){
        bookService.deleteBook(book_id);
        return ResponseEntity.ok(new OKMessageResponse<>("Livre supprimé avec succès."));
    }
}
