package org.irfan.library.controllers;

import org.irfan.library.dto.GetAuthorDTO;
import org.irfan.library.dto.GetAuthorRequest;
import org.irfan.library.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/all")
    public List<GetAuthorDTO> getAllAuthors() {
        System.out.println("prout");
        return authorService.getAllAuthors();
    }

    @GetMapping("/test")
    public String test() {
        return "prout";
    }

    @GetMapping("/")
    public ResponseEntity<?> getAuthor(@ModelAttribute GetAuthorRequest request) {
        return authorService.getAuthorByName(request.getFirstName())
                .map(author -> ResponseEntity.ok().body(author))  // 200 OK with body
                .orElse(ResponseEntity.notFound().build());  // 404 Not Found without body
    }
}
