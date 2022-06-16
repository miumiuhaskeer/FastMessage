package com.miumiuhaskeer.fastmessage.exception;

import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;

public class RefreshTokenExpiredException extends RuntimeException {

    public RefreshTokenExpiredException() {
        super(ErrorBundle.get("error.refreshTokenExpiredException.message"));
    }
}
