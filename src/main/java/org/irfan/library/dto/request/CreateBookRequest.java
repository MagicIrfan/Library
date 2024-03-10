package org.irfan.library.dto.request;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {
    @NotNull
    @Id
    private Integer author_id;
    @NotNull
    @Id
    private Integer booktype_id;
    @NotEmpty
    private String title;
}
