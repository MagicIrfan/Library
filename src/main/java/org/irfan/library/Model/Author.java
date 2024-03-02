package org.irfan.library.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public Author(String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
        this.books = new ArrayList<>();
    }
}
