package com.kute.webflux.handler;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * created by bailong001 on 2019/04/10 16:50
 */
public abstract class AbstractHandler {

    public <T, P extends Publisher<T>> Mono<ServerResponse> responseJson(P publisher, Class<T> elementClass) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(publisher, elementClass);
    }

}
