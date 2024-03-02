package org.irfan.library.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
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
}
