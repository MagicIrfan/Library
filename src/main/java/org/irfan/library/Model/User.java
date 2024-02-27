package org.irfan.library.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column
    @NotEmpty
    @Size(min = 5, max = 16, message = "Le pseudo doit contenir entre 5 et 16 caractères")
    private String username;

    @Column
    @NotEmpty
    @Email(message="Merci de saisir un e-mail valide")
    private String email;

    @Column
    @NotEmpty
    @Size(min = 5, message = "Le mot de passe doit contenir au moins 5 caractères")
    private String password;

    public User(String username, String email, String password, Role role){
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}