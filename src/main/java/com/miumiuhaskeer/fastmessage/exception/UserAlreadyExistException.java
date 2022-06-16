package com.miumiuhaskeer.fastmessage.exception;

import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException() {
        super(ErrorBundle.get("error.userAlreadyExistException.message"));
    }
}
