package com.miumiuhaskeer.fastmessage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ApplicationPropertiesTest {

    @Value("${spring.datasource.driver-class-name}")
    private String driverName;

    @Test
    public void hasH2DriverTest() {
        assertEquals(driverName, "org.h2.Driver");
    }
}
