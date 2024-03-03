package org.irfan.library.dto.request;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
