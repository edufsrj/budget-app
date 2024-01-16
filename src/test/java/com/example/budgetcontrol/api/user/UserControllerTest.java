package com.example.budgetcontrol.api.user;

import com.example.budgetcontrol.api.response.Response;
import com.example.budgetcontrol.domain.DomainModelFaker;
import com.example.budgetcontrol.domain.user.UserDTO;
import com.example.budgetcontrol.domain.user.UserService;
import com.example.budgetcontrol.infra.base.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
public class UserControllerTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebTestClient client;

    @MockBean
    private UserService userService;

    private UserControllerImpl underTest = new UserControllerImpl(userService);

    @Test
    void getUsers() {
        //given
        var fakeUserDto = new UserDTO(DomainModelFaker.fakeId(), "FAKE_NICKNAME", "FAKE_EMAIL","FAKE_PWD", Status.ACTIVE);
        var userListDTO = List.of(fakeUserDto, fakeUserDto, fakeUserDto);

        //when
        when(userService.getAllUsers()).thenReturn(Flux.fromIterable(userListDTO));

        //then
        client.get().uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                    assertAll(
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode())
                    );

                    var responseDtoList = List.of(
                            objectMapper.convertValue(response.getPayload(), UserResponseDTO[].class));

                    assertEquals(3, responseDtoList.size());

                    responseDtoList.forEach(responseDto ->
                            assertAll(
                                    () -> assertEquals(fakeUserDto.id(), responseDto.id()),
                                    () -> assertEquals(fakeUserDto.email(), responseDto.email()),
                                    () -> assertEquals(fakeUserDto.nickName(), responseDto.nickName()),
                                    () -> assertEquals(fakeUserDto.password(), responseDto.password()),
                                    () -> assertEquals(fakeUserDto.status(), responseDto.status())
                            ));

                });

        verify(userService).getAllUsers();
    }
}
