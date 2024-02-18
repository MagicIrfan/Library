package org.irfan.library.controllers;

import org.irfan.library.dto.GetAuthorDTO;
import org.irfan.library.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public List<GetAuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/author")
    public ResponseEntity<?> getAuthorByName(@RequestParam String name) {
        return authorService.getAuthorByName(name)
                .map(author -> ResponseEntity.ok().body(author))  // 200 OK with body
                .orElse(ResponseEntity.notFound().build());  // 404 Not Found without body
    }
}
