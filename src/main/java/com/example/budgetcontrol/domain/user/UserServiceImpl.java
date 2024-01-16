package com.example.budgetcontrol.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserPersistencePort userPersistencePort;

    @Override
    public Flux<UserDTO> getAllUsers() {
        return userPersistencePort.getAllUsers();
    }

    @Override
    public Mono<UserDTO> getUserById(UUID id) {
        return userPersistencePort.getUserById(id);
    }

    @Override
    public Mono<UserDTO> addUser(UserDTO userDTO) {
        return userPersistencePort.addUser(userDTO);
    }

    @Override
    public Mono<UserDTO> updateUser(UserDTO userDTO) {
        return userPersistencePort.updateUser(userDTO);
    }

    @Override
    public Mono<Void> removeUser(UUID id) {
        return userPersistencePort.removeUser(id);
    }
}
