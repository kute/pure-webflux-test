package com.kute.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

/**
 * created by bailong001 on 2019/03/05 14:33
 */
@Configuration
public class StaticRouteConfig {

    @Bean
    public RouterFunction<?> staticRouterFunction() {
        return route(GET("/ni/alluser"), request ->
                status(HttpStatus.FOUND).location(URI.create(request.path().replace("ni", "user"))).build())
                ;
    }
}
