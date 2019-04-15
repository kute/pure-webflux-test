package com.kute.webflux.controller;

import com.google.common.base.Preconditions;
import com.kute.webflux.entity.User;
import com.kute.webflux.handler.UserHandler;
import com.kute.webflux.repository.UserRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * created by bailong001 on 2019/02/22 16:04
 * <p>
 * spring mvc 响应式模式
 */
@RestController
@RequestMapping("/flux")
public class ReactorController {

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

    @GetMapping("/webclient")
    public Flux<User> webclient() {
        return WebClient.create("http://localhost:8090")
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/flux/alluser")
                                .queryParam("t", System.currentTimeMillis())
                                .build())
                .exchange()
                .flatMapMany(clientResponse ->
                        clientResponse.bodyToFlux(User.class));
    }

    @PostMapping("/save")
    public Mono<User> save(User user) {
        Preconditions.checkNotNull(user);
        return userHandler.doSave(user);
    }

    /**
     * TODO
     *
     * @param userPublisher
     * @return
     */
    @PostMapping("/save2")
    public Mono<User> save2(@RequestBody Publisher<User> userPublisher) {
        return null;
    }

    /**
     * 响应式阻塞
     *
     * 测试压测：
     *
     * @param seconds
     * @return
     */
    @GetMapping("/latency/{seconds}")
    public Mono<String> latency(@PathVariable Long seconds) {
        return Mono.just("reactor/latency")
                .delayElement(Duration.ofSeconds(seconds));
    }

}
