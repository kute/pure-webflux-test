package com.kute.webflux.handler;

import com.kute.webflux.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * created by bailong001 on 2019/02/26 09:56
 */
@Component
public class UserHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHandler.class);

    public Mono<ServerResponse> getAllUser(ServerRequest request) {
        return ServerResponse.ok().body(Flux.range(1, 10).map(String::valueOf).map(User::new), User.class);
    }

    public Mono<ServerResponse> getUser(Integer id) {
        return ServerResponse.ok().body(Mono.just(id).map(String::valueOf).map(User::new), User.class);
    }

    public Mono<ServerResponse> currentTime(ServerRequest request) {
        return ServerResponse.ok()
                // 基于 HTML5 的 server-send event 服务器推送
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(Flux.interval(Duration.ofSeconds(1))
                        .publishOn(Schedulers.newElastic("kute"))
                        .map(i -> {
                            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            LOGGER.info("基于 HTML5 的 server-send event 服务器推送:{}", date);
                            return date;
                        }), String.class);
    }

}
