package org.irfan.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookWithoutAuthorDTO {
    String title;
    BookTypeDTO bookType;
}
