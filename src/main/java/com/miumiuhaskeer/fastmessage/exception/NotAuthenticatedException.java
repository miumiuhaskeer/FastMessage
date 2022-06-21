package com.miumiuhaskeer.fastmessage.exception;

import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;

public class NotAuthenticatedException extends RuntimeException {

    public NotAuthenticatedException() {
        super(ErrorBundle.get("error.notAuthenticatedException.message"));
    }
}
