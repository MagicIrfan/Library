package org.irfan.library.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookTypeRequest {
    @NotEmpty
    String name;
}
