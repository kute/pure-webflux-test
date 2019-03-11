package com.kute.webflux.repository;

import com.kute.webflux.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * created by bailong001 on 2019/03/11 20:14
 *
 *
 */
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {

    /**
     * 方法名 可以 自动填充完成
     * @param name
     * @return
     */
    Mono<User> findUserByName(String name);

    Mono<Long> deleteByName(String name);

}
