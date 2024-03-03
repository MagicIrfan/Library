package org.irfan.library.controllers;

import jakarta.validation.Valid;
import org.irfan.library.Model.Author;
import org.irfan.library.Model.Type;
import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dao.BookTypeRepository;
import org.irfan.library.dto.BookDTO;
import org.irfan.library.dto.request.CreateBookRequest;
import org.irfan.library.dto.response.ErrorMessageResponse;
import org.irfan.library.dto.response.OKMessageResponse;
import org.irfan.library.services.AuthorService;
import org.irfan.library.services.BookService;
import org.irfan.library.services.BookTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookDTO>> getAllBooks(){
        List<BookDTO> books = bookService.getAllBooks();
        return !books.isEmpty() ? ResponseEntity.ok().body(books) : ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<?> getBook(@RequestParam(value = "id", required = false) Optional<Long> id,
                                     @RequestParam(value = "title", required = false) Optional<String> title){
        boolean idIsPresent = id.isPresent();
        boolean titleIsPresent = title.isPresent() && !title.get().isEmpty();

        if (idIsPresent && titleIsPresent) {
            return bookService.getBookByIdAndTitle(id.get(), title.get())
                    .map(author -> ResponseEntity.ok().body(author))
                    .orElse(ResponseEntity.notFound().build());
        } else if (idIsPresent) {
            return bookService.getBookById(id.get())
                    .map(author -> ResponseEntity.ok().body(author))
                    .orElse(ResponseEntity.notFound().build());
        } else if (titleIsPresent) {
            return bookService.getBookByTitle(title.get())
                    .map(author -> ResponseEntity.ok().body(author))
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.badRequest().body(new ErrorMessageResponse<>("Vous pouvre s'il vous-plaît"));
    }

    @PostMapping()
    public ResponseEntity<OKMessageResponse<String>> createBook(@Valid @RequestBody CreateBookRequest request){
        bookService.createBook(request);
        return ResponseEntity.ok(new OKMessageResponse<>("Livre créé avec succès."));
    }

    @PatchMapping("/:id")
    public ResponseEntity<?> editBook(){

    }
}
