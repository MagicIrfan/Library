package org.irfan.library.controllers;

import jakarta.validation.Valid;
import org.irfan.library.dto.*;
import org.irfan.library.dto.request.CreateAuthorRequest;
import org.irfan.library.dto.response.ErrorMessageResponse;
import org.irfan.library.dto.response.OKMessageResponse;
import org.irfan.library.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/author")
@CrossOrigin
public class AuthorController {

    private final AuthorService authorService;
    @Autowired
    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthors();
        return !authors.isEmpty() ? ResponseEntity.ok().body(authors) : ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<?> getAuthor(
            @RequestParam(value = "firstname", required = false) Optional<String> firstname,
            @RequestParam(value = "lastname", required = false) Optional<String> lastname) {

        boolean firstnameIsPresent = firstname.isPresent() && !firstname.get().isEmpty();
        boolean lastnameIsPresent = lastname.isPresent() && !lastname.get().isEmpty();

        if (firstnameIsPresent && lastnameIsPresent) {
            return authorService.getAuthorByFirstNameAndLastName(firstname.get(), lastname.get())
                    .map(author -> ResponseEntity.ok().body(author))
                    .orElse(ResponseEntity.notFound().build());
        } else if (firstnameIsPresent) {
            return authorService.getAuthorByFirstName(firstname.get())
                    .map(author -> ResponseEntity.ok().body(author))
                    .orElse(ResponseEntity.notFound().build());
        } else if (lastnameIsPresent) {
            return authorService.getAuthorByLastName(lastname.get())
                    .map(author -> ResponseEntity.ok().body(author))
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.badRequest().body(new ErrorMessageResponse<>("Please provide at least a first name or a last name."));
    }

    @PostMapping()
    public ResponseEntity<?> createAuthor(@Valid @RequestBody CreateAuthorRequest request){
        authorService.createAuthor(request);
        return ResponseEntity.ok(new OKMessageResponse<>("L'auteur " + request.getFirstname() + " " + request.getLastname() + " a été crée"));
    }
}
