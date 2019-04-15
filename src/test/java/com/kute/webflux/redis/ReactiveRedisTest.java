package com.kute.webflux.redis;

import com.kute.webflux.config.redis.ReactiveBaseCacheService;
import io.vavr.Tuple;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import static org.junit.Assert.*;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * created by bailong001 on 2019/04/14 15:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactiveRedisTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveRedisTest.class);

    @Resource
    private ReactiveBaseCacheService reactiveBaseCacheService;

    Tuple2<String, String> tuple2 = Tuples.of("kute", "default");

    @Before
    public void before() {
        reactiveBaseCacheService.del(tuple2.getT1());
    }

    @Test
    public void kvTest() {

        Mono<String> value = reactiveBaseCacheService.getKV(tuple2.getT1());
        assertNotNull(value);
        assertFalse(value.blockOptional().isPresent());

        Optional<Boolean> result = reactiveBaseCacheService.setKV(tuple2.getT1(), "default");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.get());

        value = reactiveBaseCacheService.getKV(tuple2.getT1());
        assertNotNull(value);
        assertTrue(value.blockOptional().isPresent());
        assertEquals(tuple2.getT2(), value.blockOptional().get());
    }

    @Test
    public void zsetTest() {

        Flux<String> flux = reactiveBaseCacheService.zrange(tuple2.getT1(), 0L, -1L);
        assertNotNull(flux);
        assertTrue(flux.hasElements().blockOptional().isPresent());
        assertFalse(flux.hasElements().blockOptional().get());
        assertTrue(flux.count().blockOptional().get() == 0L);

        IntStream.rangeClosed(1, 10)
                .boxed()
                .forEachOrdered(i -> reactiveBaseCacheService.zadd(tuple2.getT1(), i.toString(), i.doubleValue()));

        flux = reactiveBaseCacheService.zrange(tuple2.getT1(), 0L, -1L);

        assertNotNull(flux);
        assertTrue(flux.hasElements().blockOptional().isPresent());
        assertTrue(flux.hasElements().blockOptional().get());
        assertTrue(flux.count().blockOptional().get() == 10L);
        LOGGER.warn("zsetTest zrange={}", flux.collectList().block());
    }

    @Test
    public void scriptTest() {

        reactiveBaseCacheService.setKV(tuple2.getT1(), "oldValue");

        String lua = "local current = redis.call('GET', KEYS[1]);if(current == ARGV[1]) then redis.call('SET', KEYS[1], ARGV[2]);return true;else return false;end;";

        Flux<String> flux = reactiveBaseCacheService.eval(lua,
                Lists.newArrayList(tuple2.getT1()),
                Lists.newArrayList("oldValue", "newValue"));
        assertNotNull(flux);
//        assertTrue(flux.hasElements().blockOptional().isPresent());
//        assertTrue(flux.hasElements().blockOptional().get());
//        LOGGER.info("eval script return value={}", flux.collectList().block());

        Mono<String> value = reactiveBaseCacheService.getKV(tuple2.getT1());

        assertNotNull(value);
        assertTrue(value.blockOptional().isPresent());
        assertEquals("newValue", value.blockOptional().get());

    }


}
