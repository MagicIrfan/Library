package org.irfan.library.controllers;

import org.irfan.library.dto.GetAuthorDTO;
import org.irfan.library.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class HelloWorldController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/hello")
    public String hello() {
        return "Welcome, User";
    }

    @GetMapping("/user")
    public String getUser() {
        return "Welcome, User";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "Welcome, Admin";
    }

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
