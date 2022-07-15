package com.miumiuhaskeer.fastmessage.properties.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties(prefix = "fastmesssage.jwt.token")
public class JWTokenProp {
    private final String secret;
    private final String fmsSecret;
    private final long expirationSeconds;
    private final long fmsExpirationSeconds;
}
