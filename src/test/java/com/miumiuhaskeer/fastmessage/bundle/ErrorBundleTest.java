package com.miumiuhaskeer.fastmessage.bundle;

import org.junit.jupiter.api.Test;

import java.util.MissingResourceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ErrorBundleTest {

    @Test
    public void getTest() {
        assertEquals(ErrorBundle.get("error.userAlreadyExistException.message"), "User already exist");
    }

    @Test
    public void getMissingResourceTest() {
        assertThrows(MissingResourceException.class, () ->
                ErrorBundle.get("resource that not exist")
        );
    }

    @Test
    public void getFromTextPropertyTest() {
        assertEquals(ErrorBundle.getFromText("{error.userAlreadyExistException.message}"), "User already exist");
    }

    @Test
    public void getFromTextPlainTextTest() {
        assertEquals(ErrorBundle.getFromText("User already exist"), "User already exist");
    }

    @Test
    public void getFromTextMissingResourceTest() {
        assertThrows(MissingResourceException.class, () ->
                ErrorBundle.getFromText("{resource that not exist}")
        );
    }
}
