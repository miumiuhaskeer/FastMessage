package com.miumiuhaskeer.fastmessage.exception;

import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;

public class ChatNotExistException extends RuntimeException {

    public ChatNotExistException() {
        super(ErrorBundle.get("error.chatNotExistException.message"));
    }
}
