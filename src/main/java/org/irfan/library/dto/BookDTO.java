package org.irfan.library.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Integer id;
    private String title;
    private AuthorDTO author;
    private BookTypeDTO bookType;
}
