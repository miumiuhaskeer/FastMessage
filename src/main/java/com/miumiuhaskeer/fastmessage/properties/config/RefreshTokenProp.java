package com.miumiuhaskeer.fastmessage.properties.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties(prefix = "fastmesssage.refresh.token")
public class RefreshTokenProp {
    private final long expirationSeconds;
}
