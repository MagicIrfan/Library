package org.irfan.library;

import org.irfan.library.dto.GetAuthorDTO;
import org.irfan.library.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Optional<GetAuthorDTO>> getAuthorByName(@RequestParam String name) {
        Optional<GetAuthorDTO> author = authorService.getAuthorByName(name);
        System.out.println(author);
        if (author.isPresent()) {
            return ResponseEntity.ok(author); // Retourne 200 OK avec le DTO en corps
        } else {
            return ResponseEntity.notFound().build(); // Retourne 404 Not Found sans corps
        }
    }
}
