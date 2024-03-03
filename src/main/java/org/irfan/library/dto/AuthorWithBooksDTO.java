package org.irfan.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorWithBooksDTO {
    private String firstname;
    private String lastname;
    private List<BookWithoutAuthorDTO> books;
}
