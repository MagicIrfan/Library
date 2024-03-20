package org.irfan.library.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    @NotEmpty
    private String firstname;
    @Column
    @NotEmpty
    private String lastname;
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Book> books;

    public Author(String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
        this.books = new ArrayList<>();
    }
}
