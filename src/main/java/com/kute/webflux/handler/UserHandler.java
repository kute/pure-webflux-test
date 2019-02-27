package com.kute.webflux.handler;

import com.kute.webflux.entity.User;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * created by bailong001 on 2019/02/26 09:56
 */
@Component
public class UserHandler {

    public Mono<ServerResponse> getAllUser(ServerRequest request) {
        return ServerResponse.ok().body(Flux.range(1, 10).map(User::new), User.class);
    }

    public Mono<ServerResponse> getUser(Integer id) {
        return ServerResponse.ok().body(Mono.just(id).map(User::new), User.class);
    }

    public Mono<Void> create(Publisher<User> userBody) {
        return null;
    }

}
