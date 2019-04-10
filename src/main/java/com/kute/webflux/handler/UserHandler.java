package com.kute.webflux.handler;

import com.kute.webflux.entity.User;
import com.kute.webflux.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * created by bailong001 on 2019/02/26 09:56
 */
@Component
public class UserHandler extends AbstractHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHandler.class);

    @Autowired
    private UserRepository userRepository;

    public Mono<ServerResponse> getAllUser(ServerRequest request) {
        return responseJson(userRepository.findAll(), User.class);
    }

    public Mono<ServerResponse> getByName(String name) {
        return responseJson(userRepository.findUserByName(name), User.class);
    }

    public Mono<ServerResponse> getUserById(ServerRequest request) {
        return responseJson(userRepository.findById(request.pathVariable("id")), User.class);
    }

    public Mono<ServerResponse> currentTime(ServerRequest request) {
        return ok()
                // 基于 HTML5 的 server-send event 服务器推送,即SSE
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(Flux.interval(Duration.ofSeconds(1))
                        .publishOn(Schedulers.newElastic("kute"))
                        .map(i -> {
                            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            LOGGER.info("基于 HTML5 的 server-send event 服务器推送:{}", date);
                            return date;
                        }), String.class);
    }

    public Mono<ServerResponse> delayStream(ServerRequest request) {
        return ok()
                // 加此content-type 数据才会 1s一个输出到客户端，否则 会等全部数据完成后再输出到客户端
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(userRepository.findAll()
                        .doOnNext(user -> LOGGER.info("delayStream produce user={}", user))
                                // 每个元素延迟1s请求：subscribe -> onNext，这里延迟消费
                                .delayElements(Duration.ofSeconds(1L)),
                        User.class);
    }

    public Mono<ServerResponse> saveOrUpdate(ServerRequest request) {
        AtomicReference<Mono<User>> mono = new AtomicReference<>(Mono.empty());
        request.bodyToMono(User.class)
                .log()
                .subscribe(user ->
                        {
                            LOGGER.info("saveOrUpdate begin, user={}", user);
                            mono.set(doSave(user));
                        },
                        throwable -> LOGGER.info("saveOrUpdate error", throwable),
                        () -> LOGGER.info("saveOrUpdate done"));
        return responseJson(mono.get(), User.class);
    }

    public Mono<User> doSave(User user) {
        if(null == user) {
            return Mono.empty();
        }
        return userRepository.save(user)
                .onErrorResume(e -> {
                    LOGGER.warn("saveOrUpdate dumplicate user with user:{}", user);
                    return userRepository.findUserByName(user.getName())
                            .flatMap(originUser -> {
                                user.setId(originUser.getId());
                                return userRepository.save(user);
                            });
                });
    }
}
