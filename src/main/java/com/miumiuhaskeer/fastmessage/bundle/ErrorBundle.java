package com.miumiuhaskeer.fastmessage.bundle;

public class ErrorBundle {

    private static final SimpleBundle bundle = new SimpleBundle("static/error");

    /**
     * Get string property by key
     *
     * @see SimpleBundle#get(String)
     */
    public static String get(String key) {
        return bundle.get(key);
    }

    /**
     * Get string resource from property key or return plain text
     *
     * @see SimpleBundle#getFromText(String)
     */
    public static String getFromText(String text) {
        return bundle.getFromText(text);
    }
}
