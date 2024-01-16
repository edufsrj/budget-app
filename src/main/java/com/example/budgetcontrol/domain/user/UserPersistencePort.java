package com.example.budgetcontrol.domain.user;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserPersistencePort {

    Flux<UserDTO> getAllUsers();

    Mono<UserDTO> getUserById(UUID id);

    Mono<UserDTO> addUser(UserDTO userDTO);

    Mono<UserDTO> updateUser(UserDTO userDTO);

    Mono<Void> removeUser(UUID id);
}
