package org.irfan.library.dto.request;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddBookToAuthorRequest {
    @NotNull
    @Id
    private Integer booktype_id;
    @NotEmpty
    private String title;
}
