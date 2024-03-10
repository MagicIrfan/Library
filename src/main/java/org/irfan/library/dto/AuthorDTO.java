package org.irfan.library.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {
    private Integer id;
    private String firstname;
    private String lastname;
}
