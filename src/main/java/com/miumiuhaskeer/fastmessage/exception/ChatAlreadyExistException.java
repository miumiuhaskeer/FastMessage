package com.miumiuhaskeer.fastmessage.exception;

import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;

public class ChatAlreadyExistException extends RuntimeException {

    public ChatAlreadyExistException() {
        super(ErrorBundle.get("{error.chatAlreadyExistException.message}"));
    }
}
