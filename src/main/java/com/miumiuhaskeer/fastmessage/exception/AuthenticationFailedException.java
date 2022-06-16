package com.miumiuhaskeer.fastmessage.exception;

import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;
import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailedException extends AuthenticationException {

    public AuthenticationFailedException() {
        super(ErrorBundle.get("error.authenticationFailedException.message"));
    }
}
