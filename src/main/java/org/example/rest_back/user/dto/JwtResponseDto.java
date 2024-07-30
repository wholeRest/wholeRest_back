package org.example.rest_back.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponseDto {
    private String msg;
    private int statusCode;
    private String accessToken;
    private String refreshToken;
}
