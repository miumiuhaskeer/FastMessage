package com.miumiuhaskeer.fastmessage.properties.bundle;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class SimpleBundle {

    private final ResourceBundle bundle;

    /**
     * Gets a resource bundle using the specified base name
     *
     * @param baseName name or path to properties file without extension
     * @throws NullPointerException – if baseName is null
     * @throws MissingResourceException – if no resource bundle for the specified base name can be found
     */
    SimpleBundle(String baseName) {
        bundle = ResourceBundle.getBundle(baseName);
    }

    /**
     * Get string property by key
     *
     * @param key string property key
     * @return string property
     * @throws NullPointerException – if key is null
     * @throws MissingResourceException – if no object for the given key can be found
     * @throws ClassCastException – if the object found for the given key is not a string
     */
    public String get(String key) {
        return bundle.getString(key);
    }

    /**
     * Get string resource from property key or return plain text
     *
     * @param text some plain text or property or is the key if surrounded by brackets (example: {key}}
     * @return string property or text parameter
     * @throws NullPointerException – if key is null
     * @throws MissingResourceException – if no object for the given key can be found
     * @throws ClassCastException – if the object found for the given key is not a string
     */
    public String getFromText(String text) {
        if (text.startsWith("{") && text.endsWith("}")) {
            return get(text.substring(1, text.length() - 1));
        }

        return text;
    }
}
