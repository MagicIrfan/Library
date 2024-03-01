package org.irfan.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String username;
    @NotEmpty(message = "Le mot de passe ne doit pas être vide")
    @Size(min = 5, message = "Le mot de passe doit contenir au moins 5 caractères")
    private String password;
    @Email
    private String email;
}
