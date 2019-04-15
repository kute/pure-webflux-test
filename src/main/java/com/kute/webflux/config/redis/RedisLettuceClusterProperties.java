package com.kute.webflux.config.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;

/**
 * created by bailong001 on 2018/10/08 15:55
 */
public class RedisLettuceClusterProperties implements InitializingBean {

    private String nodes;

    private String maxRedirects;

    private Long timeout;

    private String password;

    private GenericObjectPoolConfig poolConfig;

    public String getNodes() {
        return nodes;
    }

    public RedisLettuceClusterProperties setNodes(String nodes) {
        this.nodes = nodes;
        return this;
    }

    public String getMaxRedirects() {
        return maxRedirects;
    }

    public RedisLettuceClusterProperties setMaxRedirects(String maxRedirects) {
        this.maxRedirects = maxRedirects;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public RedisLettuceClusterProperties setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RedisLettuceClusterProperties setPassword(String password) {
        this.password = password;
        return this;
    }

    public GenericObjectPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public RedisLettuceClusterProperties setPoolConfig(GenericObjectPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
        return this;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
