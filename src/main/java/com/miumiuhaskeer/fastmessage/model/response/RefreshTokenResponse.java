package com.miumiuhaskeer.fastmessage.model.response;

import lombok.Data;

@Data
public class RefreshTokenResponse {
    private final String accessToken;
    private final String refreshToken;
}
