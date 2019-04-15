package com.kute.webflux.config;

import com.kute.webflux.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * created by bailong001 on 2019/03/05 14:32
 *
 * 函数式 响应式模式
 *
 * 1. HandlerFunction相当于Controller中的具体处理方法，输入为请求，输出为装在Mono中的响应
 * 2. RouterFunction，顾名思义，路由，相当于@RequestMapping，用来判断什么样的url映射到那个具体的HandlerFunction，
 * 输入为请求，输出为装在Mono里边的Handlerfunction
 */
@Configuration
public class UserRouteConfig {

    @Autowired
    private UserHandler userHandler;

    @Bean
    public RouterFunction<ServerResponse> userRouterFunction() {
        return route(GET("/user/alluser"), userHandler::getAllUser)
                .andRoute(GET("/user/getuser/{id}"), userHandler::getUserById)
                .andRoute(GET("/user/getbyname/{name}"), request -> {
                    String name = request.pathVariable("name");
                    return userHandler.getByName(name);
                })
                // 每秒输出当前时间，服务器推送
                .andRoute(GET("/user/time"), userHandler::currentTime)
                // application/stream+json
                .andRoute(GET("/user/stream"), userHandler::delayStream)
                // alluser
                .andRoute(GET("/user/alluser"), userHandler::getAllUser)
                // bodyToMono，等价于 ApiController.save2()
                .andRoute(POST("/user/save"), userHandler::saveOrUpdate)

                ;
    }

}
