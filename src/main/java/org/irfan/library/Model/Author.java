package org.irfan.library.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotEmpty
    private String firstname;
    @Column
    @NotEmpty
    private String lastname;
    @OneToMany(mappedBy = "author")
    private List<Book> books;
}
