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
    protected ReactiveRedisTemplate<String, String> lettuceReactiveRedisTemplate;

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
        return lettuceReactiveRedisTemplate.delete(keyList).blockOptional();
    }

    public Optional<Long> del(String key) {
        return lettuceReactiveRedisTemplate.delete(key).blockOptional();
    }

    public Optional<Long> del(Publisher<String> keys) {
        return lettuceReactiveRedisTemplate.delete(keys).blockOptional();
    }

    public Mono<Boolean> exists(String key) {
        return lettuceReactiveRedisTemplate.hasKey(key);
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
        return lettuceReactiveRedisTemplate.opsForValue();
    }

    public ReactiveHashOperations<String, String, String> opsForHash() {
        return lettuceReactiveRedisTemplate.opsForHash();
    }

    public ReactiveZSetOperations<String, String> opsForZSet() {
        return lettuceReactiveRedisTemplate.opsForZSet();
    }

    public ReactiveSetOperations<String, String> opsForSet() {
        return lettuceReactiveRedisTemplate.opsForSet();
    }

    public ReactiveListOperations<String, String> opsForList() {
        return lettuceReactiveRedisTemplate.opsForList();
    }

    public ReactiveHyperLogLogOperations<String, String> opsForHyperLogLog() {
        return lettuceReactiveRedisTemplate.opsForHyperLogLog();
    }

    public ReactiveGeoOperations<String, String> opsForGeo() {
        return lettuceReactiveRedisTemplate.opsForGeo();
    }

    public RedisSerializationContext<String, String> serializationContext() {
        return lettuceReactiveRedisTemplate.getSerializationContext();
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
        return lettuceReactiveRedisTemplate.expire(key, duration).blockOptional();
    }

    public Optional<Boolean> expire(String key, Instant instant) {
        return lettuceReactiveRedisTemplate.expireAt(key, instant).blockOptional();
    }

    @Deprecated
    public Flux<String> keys(String pattern) {
        return lettuceReactiveRedisTemplate.keys(pattern);
    }

    public Flux<Object> flushdb(String pattern, int limitPerDel) {
        if (Strings.isNullOrEmpty(pattern) || limitPerDel > 1000) {
            return Flux.error(new IllegalArgumentException("Pattern is null or limitPerDel is illegal(<=1000)"));
        }
        Flux<Object> flux = Flux.empty();
        Mono.fromCallable(() -> ScanOptions.scanOptions().match(pattern).count(limitPerDel).build())
                .subscribe(scanOptions -> flux.concatWith(lettuceReactiveRedisTemplate.execute(connection -> {
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
                    lettuceReactiveRedisTemplate.execute(connection -> {
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
            return lettuceReactiveRedisTemplate.execute(redisScript, finalKeys, finalArgs)
                    .collectList()
                    .flatMapMany(Flux::fromIterable);
        })
                .onFailure(ex -> LOGGER.error("eval lusscript error, script={}", luaScript, ex))
                .getOrElse(Flux.empty());
    }

}
