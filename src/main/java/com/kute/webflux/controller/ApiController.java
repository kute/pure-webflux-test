package com.kute.webflux.controller;

import com.kute.webflux.entity.User;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * created by bailong001 on 2019/02/22 16:04
 */
@RestController
@RequestMapping("/flux")
public class ApiController {

    @GetMapping("/index")
    public Mono<String> index() {
        return Mono.just("spring 5 webflux");
    }

    @GetMapping("/alluser")
    public Flux<User> alluser() {
        return Flux.range(1, 10).map(String::valueOf).map(User::new);
    }

    @GetMapping("/create")
    public Mono<Void> create(@RequestBody Publisher<User> userBody) {
        return Mono.empty();
    }


}
