package com.example.budgetcontrol.infra.user;

import com.example.budgetcontrol.domain.exceptions.UserNotFoundException;
import com.example.budgetcontrol.domain.user.UserDTO;
import com.example.budgetcontrol.domain.user.UserPersistencePort;
import com.example.budgetcontrol.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserRepository userRepository;

    @Override
    public Flux<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .map(this::mapToUserDTO);
    }

    @Override
    public Mono<UserDTO> getUserById(UUID id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("id", id.toString())))
                .map(this::mapToUserDTO);
    }

    @Override
    public Mono<UserDTO> addUser(UserDTO userDTO) {
        return userRepository.save(this.mapToUserEntity(userDTO))
                .map(this::mapToUserDTO);
    }

    @Override
    public Mono<UserDTO> updateUser(UserDTO userDTO) {
        return userRepository.findById(userDTO.id())
                .flatMap(userEntity -> {
                    userEntity.setPassword(DigestUtils.md5DigestAsHex(userDTO.password().getBytes()));
                    userEntity.setEmail(userDTO.email());
                    userEntity.setNickName(userDTO.nickName());
                    userEntity.setStatus(userDTO.status());
                    userEntity.setUpdateTime(Utils.getTimeNow());

                    return userRepository.save(userEntity);
                })
                .switchIfEmpty(Mono.error(new UserNotFoundException("id", userDTO.id().toString())))
                .map(this::mapToUserDTO);
    }

    @Override
    public Mono<Void> removeUser(UUID id) {
        return userRepository.deleteById(id);
    }

    protected UserDTO mapToUserDTO(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .nickName(userEntity.getNickName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .status(userEntity.getStatus())
                .build();
    }

    protected UserEntity mapToUserEntity(UserDTO userDTO) {
        return UserEntity.builder()
                .id(userDTO.id())
                .nickName(userDTO.nickName())
                .email(userDTO.email())
                .password(DigestUtils.md5DigestAsHex(userDTO.password().getBytes()))
                .status(userDTO.status())
                .build();
    }
}
