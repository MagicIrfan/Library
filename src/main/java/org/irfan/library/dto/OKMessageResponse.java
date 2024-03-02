package org.irfan.library.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OKMessageResponse<T> {
    T message;
}
