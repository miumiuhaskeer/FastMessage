package com.miumiuhaskeer.fastmessage.model.response;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private final Long id;
    private final String email;
    private final List<String> roles;
    private final String accessToken;
    private final String refreshToken;
}
