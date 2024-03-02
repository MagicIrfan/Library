package org.irfan.library.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDTO {
    private String firstname;
    private String lastname;
    public AuthorDTO(String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
