package com.miumiuhaskeer.fastmessage.properties.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "fastmesssage.refresh.token")
public class RefreshTokenConfig {
    private long expirationSeconds;
}
