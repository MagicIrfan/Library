package org.irfan.library.controllers;

import jakarta.validation.Valid;
import org.irfan.library.dto.UserDTO;
import org.irfan.library.dto.request.EditUserRequest;
import org.irfan.library.dto.response.OKMessageResponse;
import org.irfan.library.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OKMessageResponse<String>> editUser(@PathVariable Integer id, @Valid @RequestBody EditUserRequest request) {
        userService.editUser(id,request);
        return ResponseEntity.ok().body(new OKMessageResponse<>("L'utilisateur a été modifié"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OKMessageResponse<String>> deleteUser(@PathVariable Integer id, @Valid @RequestBody EditUserRequest request) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body(new OKMessageResponse<>("L'utilisateur a été supprimé"));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return !users.isEmpty() ? ResponseEntity.ok().body(users) : ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<UserDTO> getUser(@RequestParam(name = "id", required = false) Optional<Integer> id,
                                          @RequestParam(name = "username", required = false) Optional<String> username,
                                          @RequestParam(name = "email", required = false) Optional<String> email) {
        Optional<UserDTO> userDTO = userService.getUser(id,username,email);
        return userDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "id") Integer id){
        Optional<UserDTO> userDTO = userService.getUserById(id);
        return userDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
