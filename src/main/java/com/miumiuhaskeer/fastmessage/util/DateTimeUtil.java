package com.miumiuhaskeer.fastmessage.util;

import java.time.LocalDateTime;

public final class DateTimeUtil {

    private DateTimeUtil() {
        throw new UnsupportedOperationException();
    }

    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }
}
