package org.irfan.library.dto.request;

import jakarta.persistence.Id;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditBookRequest {
    @Id
    private Integer author_id;
    @Id
    private Integer booktype_id;
    private String title;
}
