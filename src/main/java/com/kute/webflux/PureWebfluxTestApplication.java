package com.kute.webflux;

import com.kute.webflux.handler.UserHandler;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.URI;
import java.util.Optional;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@SpringBootApplication
public class PureWebfluxTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(PureWebfluxTestApplication.class, args);

    }

    @Bean
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler userHandler) {
        return route(GET("/user/alluser"), userHandler::getAllUser)
                .andRoute(GET("/user/getuser/{id}"), request -> {
                    String id = Optional.of(request.pathVariable("id")).orElse(RandomStringUtils.randomNumeric(3));
                    return userHandler.getUser(Integer.parseInt(id));
                });
    }

    @Bean
    public RouterFunction<?> staticRouterFunction() {
        return route(GET("/ni/alluser"), request ->
                status(HttpStatus.FOUND).location(URI.create(request.path().replace("ni", "user"))).build())
                ;
    }

}
