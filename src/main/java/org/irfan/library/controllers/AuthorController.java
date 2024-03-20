package org.irfan.library.controllers;

import jakarta.validation.Valid;
import org.irfan.library.dto.*;
import org.irfan.library.dto.request.CreateAuthorRequest;
import org.irfan.library.dto.request.EditAuthorRequest;
import org.irfan.library.dto.response.OKMessageResponse;
import org.irfan.library.services.AuthorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/authors")
@CrossOrigin
public class AuthorController {

    private final AuthorServiceImpl authorService;
    @Autowired
    public AuthorController(AuthorServiceImpl authorService){
        this.authorService = authorService;
    }

    @GetMapping()
    public ResponseEntity<List<AuthorDTO>> getAuthors(
            @RequestParam(value = "id", required = false) Optional<Integer> id,
            @RequestParam(value = "firstname", required = false) Optional<String> firstname,
            @RequestParam(value = "lastname", required = false) Optional<String> lastname,
            @RequestParam(value = "bookId", required = false) Optional<Integer> bookId) {
        return ResponseEntity.ok().body(authorService.getAuthorsByCriteria(id,firstname,lastname,bookId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Integer id) {
        AuthorDTO authorDTO = authorService.getAuthorById(id);
        return ResponseEntity.ok(authorDTO);
    }

    @PostMapping()
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody CreateAuthorRequest request){
        AuthorDTO newAuthor = authorService.createAuthor(request);
        return ResponseEntity.ok(newAuthor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorDTO> editAuthor(@PathVariable Integer id, @Valid @RequestBody EditAuthorRequest request){
        AuthorDTO editedAuthor = authorService.editAuthor(id, request);
        return ResponseEntity.ok(editedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OKMessageResponse<String>> deleteAuthor(@PathVariable Integer id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.ok(new OKMessageResponse<>("L'auteur est supprimé"));
    }
}
