package com.qm.user;

import com.qm.user.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceApplicationTests {

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate restTemplate;

    private String url(String path) { return "http://localhost:" + port + path; }

    @Test void contextLoads() {}

    @Test
    void testRegisterAndLogin() {
        RegisterRequest reg = new RegisterRequest();
        reg.setName("Test User"); reg.setEmail("uc19test@test.com"); reg.setPassword("pass123");
        ResponseEntity<String> regResponse = restTemplate.postForEntity(url("/auth/register"), reg, String.class);
        assertEquals(HttpStatus.OK, regResponse.getStatusCode());

        AuthRequest login = new AuthRequest();
        login.setEmail("uc19test@test.com"); login.setPassword("pass123");
        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity(url("/auth/login"), login, AuthResponse.class);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody().getToken());
    }
}
