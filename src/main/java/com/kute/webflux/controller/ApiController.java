package com.kute.webflux.controller;

import com.kute.webflux.entity.User;
import com.kute.webflux.service.UserService;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * created by bailong001 on 2019/02/22 16:04
 */
@RestController
@RequestMapping("/flux")
public class ApiController {

    @Resource
    private UserService userService;

    @GetMapping("/index")
    public Mono<String> index() {
        return Mono.just("spring 5 webflux");
    }

    @GetMapping("/alluser")
    public Flux<User> alluser() {
        return userService.getAllUser();
    }

    @GetMapping("/create")
    public Mono<Void> create(@RequestBody Publisher<User> userBody) {
        return userService.create(userBody);
    }


}
