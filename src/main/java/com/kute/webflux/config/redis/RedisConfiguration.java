package com.kute.webflux.config.redis;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

/**
 * created by bailong001 on 2019/04/14 11:32
 */
@Configuration
public class RedisConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfiguration.class);

    /**
     * lettuce redis-cluster
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = {"nodes", "maxRedirects"}, prefix = "spring.redis.reactive.lettucecluster")
    @ConfigurationProperties(prefix = "spring.redis.reactive.lettucecluster")
    public RedisLettuceClusterProperties lettuceClusterProperties() {
        return new RedisLettuceClusterProperties();
    }

    @Bean
    @ConditionalOnBean(value = RedisLettuceClusterProperties.class)
    public LettuceConnectionFactory reactiveLettuceClusterConnectionFactory() {
        RedisLettuceClusterProperties redisLettuceClusterProperties = lettuceClusterProperties();
        LOGGER.info("Init redis reactiveLettuceClusterConnectionFactory with properties:{}", JSONObject.toJSONString(redisLettuceClusterProperties));

        Map<String, Object> map = Maps.newHashMap(
                ImmutableMap.of("spring.redis.cluster.nodes", redisLettuceClusterProperties.getNodes(),
                        "spring.redis.cluster.max-redirects", redisLettuceClusterProperties.getMaxRedirects())
        );

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("redisClusterConfiguration", map));

        if (!Strings.isNullOrEmpty(redisLettuceClusterProperties.getPassword())) {
            redisClusterConfiguration.setPassword(RedisPassword.of(redisLettuceClusterProperties.getPassword()));
        }

        LettuceClientConfiguration clientPollConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(redisLettuceClusterProperties.getTimeout()))
                .poolConfig(redisLettuceClusterProperties.getPoolConfig())
                .build();

        return new LettuceConnectionFactory(redisClusterConfiguration, clientPollConfig);
    }

    @Bean(name = "lettuceReactiveRedisTemplate")
    @ConditionalOnBean(value = RedisLettuceClusterProperties.class)
    public ReactiveRedisOperations<String, String> lettuceReactiveRedisTemplate() {

        LOGGER.info("Init reactiveRedisTemplateForLettuceCluster ...");

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        RedisSerializationContext<String, String> redisSerializationContext =
                RedisSerializationContext.<String, String>newSerializationContext()
                        .key(stringRedisSerializer)
                        .value(stringRedisSerializer)
                        .hashKey(stringRedisSerializer)
                        .hashValue(stringRedisSerializer)
                        .build();

        return new ReactiveRedisTemplate<>(reactiveLettuceClusterConnectionFactory(),
                redisSerializationContext);
    }
}
