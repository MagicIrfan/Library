package org.irfan.library.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditUserRequest {
    @Size(min = 4, message = "Le pseudo doit contenir au moins 4 caractères")
    private String username;
    @Size(min = 5, message = "Le mot de passe doit contenir au moins 5 caractères")
    private String password;
    @Email
    private String email;
}
