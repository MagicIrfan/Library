package org.irfan.library.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookDTO {
    String title;
    AuthorDTO author;
    BookTypeDTO bookType;
}
