package com.miumiuhaskeer.fastmessage.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "from")
public class SimpleError {

    private final String name;
    private final String message;

    public static SimpleError fromException(Exception e) {
        return new SimpleError(
                e.getClass().toString(),
                e.getMessage()
        );
    }
}
