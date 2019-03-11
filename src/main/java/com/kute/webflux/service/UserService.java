package com.kute.webflux.service;

import com.kute.webflux.entity.User;
import com.kute.webflux.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * created by bailong001 on 2019/03/11 20:18
 */
@Service
public class UserService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public Mono<ServerResponse> getByName(String name) {
        return response(userRepository.findUserByName(name), User.class);
    }

    public Mono<ServerResponse> findAll() {
        return response(userRepository.findAll(), User.class);
    }

    public Mono<ServerResponse> save(User user) {
        return response(userRepository.save(user)
                .onErrorResume(e -> userRepository.findUserByName(user.getName())
                        .flatMap(originUser -> {
                            user.setId(originUser.getId());
                            return userRepository.save(user);
                        })), User.class);
    }
}
