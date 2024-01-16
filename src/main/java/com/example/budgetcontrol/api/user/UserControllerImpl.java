package com.example.budgetcontrol.api.user;

import com.example.budgetcontrol.api.response.Response;
import com.example.budgetcontrol.domain.exceptions.ResourceNotFoundException;
import com.example.budgetcontrol.domain.user.UserDTO;
import com.example.budgetcontrol.domain.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<Response> getusers() {
        return userService.getAllUsers()
                .map(mapToUserResponseDTO())
                .collectList()
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> getUserById(UUID id) {
        return userService.getUserById(id)
                .map(mapToUserResponseDTO())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> addUser(UserRequestDTO userRequestDTO) {
        return userService.addUser(mapToUserDto().apply(userRequestDTO))
                .map(mapToUserResponseDTO())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> updateUser(UUID id, UserRequestDTO userRequestDTO) {
        return userService.updateUser(mapToUserDto().apply(userRequestDTO))
                .map(mapToUserResponseDTO())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> removeUser(UUID id) {
        return userService.removeUser(id)
                .map(Response.noContent()::setPayload)
                .cast(Response.class)
                .onErrorResume(ResourceNotFoundException.class, ex -> {
                    var response = Response.exception();
                    response.addErrorMsgToResponse("Error while removing user", ex);
                    return Mono.just(response);
                });
    }

    @Override
    public Function<UserDTO, UserResponseDTO> mapToUserResponseDTO() {
        return  userDTO ->
                UserResponseDTO.builder()
                    .id(userDTO.id())
                    .nickName(userDTO.nickName())
                    .email(userDTO.email())
                    .password(userDTO.password())
                    .status(userDTO.status())
                    .build();
    }

    @Override
    public Function<UserRequestDTO, UserDTO> mapToUserDto() {
        return useRequestDTO ->
                UserDTO.builder()
                    .id(useRequestDTO.id())
                    .nickName(useRequestDTO.nickName())
                    .email(useRequestDTO.email())
                    .password(useRequestDTO.password())
                    .status(useRequestDTO.status())
                    .build();
    }
}
