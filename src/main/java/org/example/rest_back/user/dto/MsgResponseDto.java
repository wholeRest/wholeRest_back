package org.example.rest_back.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class MsgResponseDto {
    private String msg;
    private int statusCode;
}
