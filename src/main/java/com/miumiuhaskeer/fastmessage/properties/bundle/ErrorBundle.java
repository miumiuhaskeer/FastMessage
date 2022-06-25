package com.miumiuhaskeer.fastmessage.properties.bundle;

public class ErrorBundle {

    private static final SimpleBundle bundle = new SimpleBundle("static/error");

    /** {@inheritDoc} */
    public static String get(String key) {
        return bundle.get(key);
    }

    /** {@inheritDoc} */
    public static String getFromText(String text) {
        return bundle.getFromText(text);
    }
}
