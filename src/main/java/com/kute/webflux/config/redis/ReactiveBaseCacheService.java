package com.kute.webflux.config.redis;

import com.google.common.base.Strings;
import io.vavr.control.Try;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * created by bailong001 on 2018/10/08 18:22
 */
@Component
public class ReactiveBaseCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveBaseCacheService.class);

    @Resource
    protected ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    public Mono<String> getKV(String key) {
        return opsForValue().get(key);
    }

    @Deprecated
    public Optional<Boolean> setKV(String key, String value) {
        return opsForValue().set(key, value).blockOptional();
    }

    public Optional<Boolean> setKV(String key, String value, Duration duration) {
        return opsForValue().set(key, value, duration).blockOptional();
    }

    public Optional<Boolean> zadd(String key, String value, Double score) {
        return opsForZSet().add(key, value, score).blockOptional();
    }

    public Optional<Long> zaddBatch(String key, Set<ZSetOperations.TypedTuple<String>> tupleSet) {
        return opsForZSet().addAll(key, tupleSet).blockOptional();
    }

    public Optional<Long> del(String... keyList) {
        return reactiveRedisTemplate.delete(keyList).blockOptional();
    }

    public Optional<Long> del(String key) {
        return reactiveRedisTemplate.delete(key).blockOptional();
    }

    public Optional<Long> del(Publisher<String> keys) {
        return reactiveRedisTemplate.delete(keys).blockOptional();
    }

    public Mono<Boolean> exists(String key) {
        return reactiveRedisTemplate.hasKey(key);
    }

    public Optional<Boolean> setNullKey(String nullKey, String nullValue) {
        return setNullKey(nullKey, nullValue, Duration.ofDays(7L));
    }

    public Optional<Boolean> setNullKey(String nullKey, String nullValue, Duration duration) {
        return opsForValue().set(nullKey, nullValue, duration).blockOptional();
    }

    public Flux<String> zrange(String key, Long lower, Long upper) {
        return opsForZSet().range(key, Range.from(Range.Bound.inclusive(lower)).to(Range.Bound.inclusive(upper)));
    }

    public Optional<Boolean> zadd(String key, String value, double score) {
        return opsForZSet().add(key, value, score).blockOptional();
    }

    public ReactiveValueOperations<String, String> opsForValue() {
        return reactiveRedisTemplate.opsForValue();
    }

    public ReactiveHashOperations<String, String, String> opsForHash() {
        return reactiveRedisTemplate.opsForHash();
    }

    public ReactiveZSetOperations<String, String> opsForZSet() {
        return reactiveRedisTemplate.opsForZSet();
    }

    public ReactiveSetOperations<String, String> opsForSet() {
        return reactiveRedisTemplate.opsForSet();
    }

    public ReactiveListOperations<String, String> opsForList() {
        return reactiveRedisTemplate.opsForList();
    }

    public ReactiveHyperLogLogOperations<String, String> opsForHyperLogLog() {
        return reactiveRedisTemplate.opsForHyperLogLog();
    }

    public ReactiveGeoOperations<String, String> opsForGeo() {
        return reactiveRedisTemplate.opsForGeo();
    }

    public RedisSerializationContext<String, String> serializationContext() {
        return reactiveRedisTemplate.getSerializationContext();
    }

    public RedisSerializationContext.SerializationPair<String> getKeySerializationPair() {
        return serializationContext().getKeySerializationPair();
    }

    public RedisSerializationContext.SerializationPair<String> getValueSerializationPair() {
        return serializationContext().getValueSerializationPair();
    }

    public RedisSerializationContext.SerializationPair<String> getHashKeySerializationPair() {
        return serializationContext().getHashKeySerializationPair();
    }

    public RedisSerializationContext.SerializationPair<String> getHashValueSerializationPair() {
        return serializationContext().getHashValueSerializationPair();
    }

    private ByteBuffer rawKey(String key) {
        return getKeySerializationPair().write(key);
    }

    private ByteBuffer rawHashKey(String key) {
        return getHashKeySerializationPair().write(key);
    }

    private ByteBuffer rawHashValue(String key) {
        return getHashValueSerializationPair().write(key);
    }

    public ByteBuffer rawValue(String value) {
        return getValueSerializationPair().write(value);
    }

    public Optional<Boolean> expire(String key, Duration duration) {
        return reactiveRedisTemplate.expire(key, duration).blockOptional();
    }

    public Optional<Boolean> expire(String key, Instant instant) {
        return reactiveRedisTemplate.expireAt(key, instant).blockOptional();
    }

    @Deprecated
    public Flux<String> keys(String pattern) {
        return reactiveRedisTemplate.keys(pattern);
    }

    public Flux<Object> flushdb(String pattern, int limitPerDel) {
        if (Strings.isNullOrEmpty(pattern) || limitPerDel > 1000) {
            return Flux.error(new IllegalArgumentException("Pattern is null or limitPerDel is illegal(<=1000)"));
        }
        Flux<Object> flux = Flux.empty();
        Mono.fromCallable(() -> ScanOptions.scanOptions().match(pattern).count(limitPerDel).build())
                .subscribe(scanOptions -> flux.concatWith(reactiveRedisTemplate.execute(connection -> {
                    connection.keyCommands().scan(scanOptions)
                            .subscribe(keyBuffer -> connection.keyCommands().del(keyBuffer).blockOptional());
                    return null;
                })));
        return flux;
    }

    public void flushdb(String pattern) {
        flushdb(pattern, 1000);
    }

    public Flux<String> keysWithScan(String pattern) {
        Flux<ByteBuffer> keyFlux = Flux.empty();
        Mono.fromCallable(() -> ScanOptions.scanOptions().match(pattern).count(1000).build())
                .subscribe(scanOptions -> {
                    reactiveRedisTemplate.execute(connection -> {
                        keyFlux.concatWith(connection.keyCommands().scan(scanOptions));
                        return null;
                    });
                });
        return keyFlux.distinct()
                .filter(Objects::nonNull)
                .map(getKeySerializationPair()::read);
    }

    @Deprecated
    public Flux<String> eval(String luaScript, List<String> keys, List<String> args) {
        if (Strings.isNullOrEmpty(luaScript)) {
            return Flux.empty();
        }
        keys = Optional.ofNullable(keys).orElse(Collections.emptyList());
        args = Optional.ofNullable(args).orElse(Collections.emptyList());

        List<String> finalKeys = keys;
        List<String> finalArgs = args;
        return Try.ofCallable(() -> {
            RedisScript<String> redisScript = new DefaultRedisScript<>(luaScript, String.class);
            return reactiveRedisTemplate.execute(redisScript, finalKeys, finalArgs)
                    .collectList()
                    .flatMapMany(Flux::fromIterable);
        })
                .onFailure(ex -> LOGGER.error("eval lusscript error, script={}", luaScript, ex))
                .getOrElse(Flux.empty());
    }

}
