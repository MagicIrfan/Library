package org.irfan.library.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAuthorDTO {
    private String name;
    public GetAuthorDTO(String name){
        this.name = name;
    }
}
