package org.irfan.library.dto.request;

import jakarta.persistence.Id;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditBookRequest {
    @Id
    private Integer authorId;
    @Id
    private Integer booktypeId;
    private String title;
}
