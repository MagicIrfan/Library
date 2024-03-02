package org.irfan.library.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateAuthorRequest {
    @NotEmpty
    public String firstname;
    @NotEmpty
    public String lastname;
}
