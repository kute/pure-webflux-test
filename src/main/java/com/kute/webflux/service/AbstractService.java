package com.kute.webflux.service;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * created by bailong001 on 2019/03/11 20:40
 */
public abstract class AbstractService {

    public  <T, P extends Publisher<T>> Mono<ServerResponse> response(P publisher, Class<T> elementClass) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(publisher, elementClass);
    }

}
