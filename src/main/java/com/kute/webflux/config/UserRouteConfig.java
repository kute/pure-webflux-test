package com.kute.webflux.config;

import com.kute.webflux.handler.UserHandler;
import com.kute.webflux.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Optional;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * created by bailong001 on 2019/03/05 14:32
 */
@Configuration
public class UserRouteConfig {

    @Autowired
    private UserHandler userHandler;

    @Autowired
    private UserService userService;

    @Bean
    public RouterFunction<ServerResponse> userRouterFunction() {
        return route(GET("/user/alluser"), userHandler::getAllUser)
                .andRoute(GET("/user/getuser/{id}"), request -> {
                    String id = Optional.of(request.pathVariable("id")).orElse(RandomStringUtils.randomNumeric(3));
                    return userHandler.getUser(Integer.parseInt(id));
                })
                // mongodb query
                .andRoute(GET("/mongo/getbyname/{name}"), request -> {
                    String name = request.pathVariable("name");
                    return userService.getByName(name);
                })
                .andRoute(GET("/mongo/getall"), request -> {
                    return userService.findAll();
                })
                // 每秒输出当前时间，服务器推送
                .andRoute(GET("/user/time"), userHandler::currentTime);
    }

}
