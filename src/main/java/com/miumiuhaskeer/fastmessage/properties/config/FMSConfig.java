package com.miumiuhaskeer.fastmessage.properties.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties(prefix = "fastmesssage.fms")
public class FMSConfig {

    private static final String HTTP = "http://";

    private final String host;
    private final Integer port;
    private final String baseUrl;

    public String getUrl() {
        return getFullHostUrl() + baseUrl;
    }

    public String getFullHostUrl() {
        return HTTP + getFullHost();
    }

    public String getFullHost() {
        return host + ":" + port;
    }
}
