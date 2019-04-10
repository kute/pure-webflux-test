package com.kute.webflux;

import com.kute.webflux.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.reactive.server.WebTestClient;

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

	    String choice = "123";

	    if(choice.contains("1")) {
            this.webClient.get()
                    .uri("/flux/alluser")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk()
                    .returnResult(User.class).getResponseBody().subscribe(user -> {
                LOGGER.warn("alluser, user={}", user);
            });
        }
        if(choice.contains("2")) {
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
        if(choice.contains("3")) {
	        this.webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/flux/save")
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
                        LOGGER.warn("flux/save done with user={}", user);
                    });

        }
        if(choice.contains("2")) {

        }
        if(choice.contains("2")) {

        }
        if(choice.contains("2")) {

        }
        if(choice.contains("2")) {

        }
        if(choice.contains("2")) {

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
