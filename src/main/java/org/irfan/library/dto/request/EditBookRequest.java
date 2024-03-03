package org.irfan.library.dto.request;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditBookRequest {
    @Id
    private Integer author_id;
    @Id
    private Integer booktype_id;
    private String title;
}
