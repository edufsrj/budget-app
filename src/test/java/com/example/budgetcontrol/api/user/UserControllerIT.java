package com.example.budgetcontrol.api.user;

import com.example.budgetcontrol.api.response.Response;
import com.example.budgetcontrol.infra.base.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
public class UserControllerIT {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webClient;

    private static UUID generatedUserId;

    @Test
    @Order(1)
    void should_save_user() {
        final var fakeUserDTO = createNewUserRequestDTO();

        webClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(createNewUserRequestDTO()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                    assertAll(
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode())
                    );

                    var responseDto = objectMapper.convertValue(response.getPayload(), UserResponseDTO.class);
                    assertNotNull(responseDto.id());
                    assertNotNull(responseDto.password());
                    assertEquals(fakeUserDTO.email(), responseDto.email());
                    assertEquals(fakeUserDTO.nickName(), responseDto.nickName());
                    assertEquals(fakeUserDTO.status(), responseDto.status());

                    generatedUserId = responseDto.id();
        });
    }

    @Test
    @Order(2)
    void should_find_user() {
        webClient.get()
                .uri("/users/" + generatedUserId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                    assertAll(
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode())
                    );

                    var responseDto = objectMapper.convertValue(response.getPayload(), UserResponseDTO.class);
                    assertEquals(generatedUserId, responseDto.id());
                    assertEquals(Status.ACTIVE, responseDto.status());

                    generatedUserId = responseDto.id();
                });
    }

    @Test
    @Order(3)
    void should_update_user() {
        var userRequestDTO = getUserRequestDTO(generatedUserId);

        webClient.put()
                .uri("/users/" + userRequestDTO.id())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userRequestDTO))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                    assertAll(
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode())
                    );

                    var responseDto = objectMapper.convertValue(response.getPayload(), UserResponseDTO.class);
                    assertEquals(userRequestDTO.id(), responseDto.id());
                    assertEquals(userRequestDTO.status(), responseDto.status());
                });
    }

    @Test
    @Order(4)
    void should_delete_user() {
        var userRequestDTO = getUserRequestDTO(generatedUserId);

        webClient.delete()
                .uri("/users/" + userRequestDTO.id())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    private UserRequestDTO createNewUserRequestDTO() {
        return UserRequestDTO.builder()
                .nickName("FAKE_NICK")
                .email("FAKE_EMAIL")
                .password("FAKE_PWD")
                .status(Status.ACTIVE)
                .build();
    }

    private UserRequestDTO getUserRequestDTO(UUID uuid) {
        return UserRequestDTO.builder()
                .id(uuid)
                .nickName("FAKE_NICK_2")
                .email("FAKE_EMAIL")
                .password("FAKE_PWD")
                .status(Status.INACTIVE)
                .build();
    }
}
