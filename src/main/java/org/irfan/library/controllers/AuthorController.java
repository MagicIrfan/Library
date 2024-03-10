package org.irfan.library.controllers;

import jakarta.validation.Valid;
import org.irfan.library.dto.*;
import org.irfan.library.dto.request.AddBookToAuthorRequest;
import org.irfan.library.dto.request.CreateAuthorRequest;
import org.irfan.library.dto.request.EditAuthorRequest;
import org.irfan.library.dto.response.ErrorMessageResponse;
import org.irfan.library.dto.response.OKMessageResponse;
import org.irfan.library.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/authors")
@CrossOrigin
public class AuthorController {

    private final AuthorService authorService;
    @Autowired
    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    @GetMapping()
    public ResponseEntity<List<AuthorWithBooksDTO>> getAuthors(
            @RequestParam(value = "id", required = false) Optional<Long> id,
            @RequestParam(value = "firstname", required = false) Optional<String> firstname,
            @RequestParam(value = "lastname", required = false) Optional<String> lastname,
            @RequestParam(value = "bookId", required = false) Optional<Long> bookId) {
        return ResponseEntity.ok().body(authorService.getAuthorsByCriteria(id,firstname,lastname,bookId));
    }

    @PostMapping()
    public ResponseEntity<OKMessageResponse<String>> createAuthor(@Valid @RequestBody CreateAuthorRequest request){
        authorService.createAuthor(request);
        return ResponseEntity.ok(new OKMessageResponse<>("L'auteur " + request.getFirstname() + " " + request.getLastname() + " a été crée"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OKMessageResponse<String>> editAuthor(@PathVariable Integer id, @Valid @RequestBody EditAuthorRequest request){
        authorService.editAuthor(id, request);
        return ResponseEntity.ok(new OKMessageResponse<>("L'auteur " + request.getFirstname() + " " + request.getLastname() + " a été crée"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OKMessageResponse<String>> deleteAuthor(@PathVariable Integer id){
        authorService.deleteAuthor(id);
        return ResponseEntity.ok(new OKMessageResponse<>("L'auteur est supprimé"));
    }

    @PostMapping("/{id}/books")
    public ResponseEntity<OKMessageResponse<String>> addBookToAuthor(@PathVariable Integer id, @Valid @RequestBody AddBookToAuthorRequest request){
        authorService.addBookToAuthor(id,request);
        return ResponseEntity.ok(new OKMessageResponse<>("Le livre a été ajouté"));
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookWithoutAuthorDTO>> getBooksOfAuthor(@PathVariable Integer id){
        List<BookWithoutAuthorDTO> books = authorService.getBooksOfAuthor(id);
        return ResponseEntity.ok(books);
    }
}
