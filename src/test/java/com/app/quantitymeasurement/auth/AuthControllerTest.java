package com.app.quantitymeasurement.auth;

import com.app.quantitymeasurement.auth.dto.AuthRequest;
import com.app.quantitymeasurement.auth.dto.AuthResponse;
import com.app.quantitymeasurement.auth.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Sparsh Garg");
        request.setEmail("sparsh@test.com");
        request.setPassword("password123");

        ResponseEntity<String> response = restTemplate.postForEntity(
            url("/auth/register"), request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("registered"));
    }

    @Test
    void testRegister_DuplicateEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("duplicate@test.com");
        request.setPassword("password123");

        restTemplate.postForEntity(url("/auth/register"), request, String.class);
        ResponseEntity<String> response = restTemplate.postForEntity(
            url("/auth/register"), request, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testLogin_Success() {
        RegisterRequest reg = new RegisterRequest();
        reg.setName("Login User");
        reg.setEmail("loginuser@test.com");
        reg.setPassword("password123");
        restTemplate.postForEntity(url("/auth/register"), reg, String.class);

        AuthRequest login = new AuthRequest();
        login.setEmail("loginuser@test.com");
        login.setPassword("password123");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
            url("/auth/login"), login, AuthResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getToken());
        assertEquals("loginuser@test.com", response.getBody().getEmail());
    }

    @Test
    void testLogin_InvalidCredentials() {
        AuthRequest login = new AuthRequest();
        login.setEmail("nonexistent@test.com");
        login.setPassword("wrongpassword");

        ResponseEntity<String> response = restTemplate.postForEntity(
            url("/auth/login"), login, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testProtectedEndpoint_WithoutToken_Returns403() {
        ResponseEntity<String> response = restTemplate.postForEntity(
            url("/api/v1/quantities/compare"), "{}", String.class);

        assertTrue(response.getStatusCode() == HttpStatus.FORBIDDEN
            || response.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testProtectedEndpoint_WithToken_Returns200() {
        RegisterRequest reg = new RegisterRequest();
        reg.setName("Token User");
        reg.setEmail("tokenuser@test.com");
        reg.setPassword("password123");
        restTemplate.postForEntity(url("/auth/register"), reg, String.class);

        AuthRequest login = new AuthRequest();
        login.setEmail("tokenuser@test.com");
        login.setPassword("password123");
        AuthResponse authResponse = restTemplate
            .postForEntity(url("/auth/login"), login, AuthResponse.class)
            .getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authResponse.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
            {
              "thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"},
              "thatQuantityDTO": {"value": 12.0, "unit": "INCH", "measurementType": "LengthUnit"}
            }
            """;

        ResponseEntity<String> response = restTemplate.exchange(
            url("/api/v1/quantities/compare"),
            HttpMethod.POST,
            new HttpEntity<>(body, headers),
            String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
