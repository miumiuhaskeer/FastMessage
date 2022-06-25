package com.miumiuhaskeer.fastmessage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class MongoConversionsConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
                Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE,
                Jsr310Converters.DateToLocalDateTimeConverter.INSTANCE
        ));
    }
}
