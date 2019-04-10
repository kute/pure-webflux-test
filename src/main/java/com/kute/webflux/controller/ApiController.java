package com.kute.webflux.controller;

import com.google.common.base.Preconditions;
import com.kute.webflux.entity.User;
import com.kute.webflux.handler.UserHandler;
import com.kute.webflux.repository.UserRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * created by bailong001 on 2019/02/22 16:04
 *
 * spring mvc 方式
 */
@RestController
@RequestMapping("/flux")
public class ApiController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserHandler userHandler;

    @GetMapping("/index")
    public Mono<String> index() {
        return Mono.just("spring 5 webflux");
    }

    @GetMapping("/alluser")
    public Flux<User> alluser() {
        return userRepository.findAll();
    }

    @PostMapping("/save")
    public Mono<User> save(User user) {
        Preconditions.checkNotNull(user);
        return userHandler.doSave(user);
    }

}
