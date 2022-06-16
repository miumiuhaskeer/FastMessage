package com.miumiuhaskeer.fastmessage.properties.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "fastmesssage.jwt.token")
public class JwtTokenConfig {
    private String secret;
    private long expirationSeconds;
}
