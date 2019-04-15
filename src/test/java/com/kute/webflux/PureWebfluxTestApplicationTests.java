package com.kute.webflux;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.kute.webflux.entity.User;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"management.endpoint.health.show-details=never"})
public class PureWebfluxTestApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(PureWebfluxTestApplicationTests.class);

    @LocalServerPort
    private int port = 9010;

    @LocalManagementPort
    private int managementPort = 9011;

    /**
     * 非阻塞、响应式 测试客户端，等价于 响应式的RestTemplate
     */
    @Autowired
    private WebTestClient webClient;

    @Test
    public void testUser() {

        Set<Integer> choice = Sets.newHashSet(
//          1,
//                2,
//                3,
                4
        );

        if (choice.contains(1)) {
            this.webClient.get()
                    .uri("/flux/alluser")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk()
                    .returnResult(User.class).getResponseBody().subscribe(user -> {
                LOGGER.warn("alluser, user={}", user);
            });
        }
        if (choice.contains(2)) {
            String name = "kute";
            this.webClient.get()
                    .uri("/user/getbyname/" + name)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(User.class).value(user -> {
                LOGGER.warn("getbyname user={}", user);
                AssertionErrors.assertEquals("wrong user name match", user.getName(), name);
            });
        }
        if (choice.contains(3)) {
            this.webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/user/save")
                            .queryParam("name", "slash")
                            .queryParam("ucId", 1000000023119361L)
                            .queryParam("birthday", 1565873040000L)
                            .queryParam("age", "20")
                            .build())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(User.class)
                    .value(user -> {
                        LOGGER.warn("user/save done with user={}", user);
                    });
        }
        //TODO 测试暂不成功
        if (choice.contains(4)) {

            User user = new User()
                    .setName("lisa2")
                    .setUcId(1000000024119361L)
                    .setAge(19)
                    .setBirthday(new Timestamp(System.currentTimeMillis()));

            Map<String, Object> bodyMap = ImmutableMap.of(
                    "name", "lisa",
                    "ucId", 1000000024119361L,
                    "age", 19,
                    "birthday", 1575873040000L
            );

            this.webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/user/save")
                            .build())
                    .accept(MediaType.APPLICATION_JSON_UTF8)
//                    .body(Mono.just(bodyMap), User.class)
                    .syncBody(user)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(User.class)
                    .value(user2 -> {
                        LOGGER.warn("user/save done with user={}", user2);
                    });
        }
    }


    class UserParameter extends ParameterizedTypeReference<User> {
        @Override
        public Type getType() {
            return super.getType();
        }
    }

    @Test
    public void testActuator() {
        this.webClient.get().uri("/actuator/health").accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk();

        this.webClient.get().uri("/actuator/info").accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk();
    }

}
