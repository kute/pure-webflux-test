package com.kute.webflux;

import com.kute.webflux.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    @LocalServerPort
    private int port = 9010;

    @LocalManagementPort
    private int managementPort = 9011;

	@Autowired
	private WebTestClient webClient;

	@Test
	public void testUser() {
	    this.webClient.get().uri("/user/alluser").accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isOk()
                .expectBodyList(User.class).hasSize(10);


	    Integer id = 8888;
	    this.webClient.get().uri("/user/getuser/" + id).accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isOk()
                .expectBody(User.class).value(user -> AssertionErrors.assertEquals("wrong user id match", user.getId(), id));
	}

    @Test
    public void testActuator() {
        this.webClient.get().uri("/actuator/health").accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk();

        this.webClient.get().uri("/actuator/info").accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk();
    }

}
