package com.kute.webflux.config;

import com.google.common.collect.Lists;
import com.kute.webflux.config.converter.MongoTypeToJavaTypeExtentionConverter;
import com.mongodb.MongoClientOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

/**
 * created by bailong001 on 2019/04/10 12:28
 */
@Configuration
public class WebConfig {

    @Autowired
    private MongoTypeToJavaTypeExtentionConverter mongoTypeToJavaTypeExtentionConverter;

    /**
     * regist converter
     *
     * @return
     */
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Lists.newArrayList(mongoTypeToJavaTypeExtentionConverter));
    }


    @Bean
    public MongoClientOptions mongoOptions() {
        return MongoClientOptions.builder()
                .connectTimeout(30000)
                .maxWaitTime(10000)
                .maxConnectionIdleTime(6000)
                .build();
    }

}
