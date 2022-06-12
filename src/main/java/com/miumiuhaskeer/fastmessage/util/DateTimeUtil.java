package com.miumiuhaskeer.fastmessage.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateTimeUtil {

    public static final String DATE_FORMAT = "dd.MM.yyyy hh:mm";

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);

    private DateTimeUtil() {
        throw new UnsupportedOperationException();
    }

    public static String currentDateTime() {
        return dateFormatter.format(new Date());
    }
}
