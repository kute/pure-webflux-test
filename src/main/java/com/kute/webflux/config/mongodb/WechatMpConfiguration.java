package com.kute.webflux.config.mongodb;

import com.mongodb.MongoClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WechatMpConfiguration {

    @Bean
    public MongoClientOptions mongoOptions() {
        return MongoClientOptions.builder()
                .connectTimeout(30000)
                .maxWaitTime(10000)
                .maxConnectionIdleTime(6000)
                .build();
    }
}