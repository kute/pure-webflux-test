package com.kute.webflux.service;

import com.kute.webflux.entity.User;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * created by bailong001 on 2019/02/22 17:22
 */
@Service
public class UserService {

    public Flux<User> getAllUser() {
        return Flux.range(1, 10).map(User::new);
    }

    public Mono<Void> create(Publisher<User> userBody) {
        return null;
    }

}
