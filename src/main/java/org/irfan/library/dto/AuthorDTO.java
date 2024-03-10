package org.irfan.library.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private List<BookWithoutAuthorDTO> books;
}
