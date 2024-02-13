package org.irfan.library;

import org.irfan.library.dto.GetAuthorDTO;
import org.irfan.library.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @ResponseBody
    public GetAuthorDTO getAuthorByName(@RequestParam String name) {
        return authorService.getAuthorByName(name);
    }
}
