package com.miumiuhaskeer.fastmessage.exception;

import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;

public class RegistrationFailedException extends RuntimeException {

    public RegistrationFailedException() {
        super(ErrorBundle.get("error.registrationFailedException.message"));
    }
}
