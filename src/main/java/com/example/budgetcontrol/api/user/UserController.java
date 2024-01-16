package com.example.budgetcontrol.api.user;

import com.example.budgetcontrol.api.response.Response;
import com.example.budgetcontrol.domain.user.UserDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

public interface UserController {

    /**
     * Handles the incoming GET request "/users"
     *
     * @return retrieve all non-deleted users
     *
     * @see com.tascigorkem.restaurantservice.api.user.UserResponseDTO
     */
    @GetMapping("/users")
    Mono<Response> getusers();

    /**
     * Handles the incoming GET request "/users/{id}"
     *
     * @param id of the User to be retrieved
     * @return User
     *
     * @see com.tascigorkem.restaurantservice.api.user.UserResponseDTO
     */
    @GetMapping("/users/{id}")
    Mono<Response> getUserById(@PathVariable("id") UUID id);

    /**
     * Handles the incoming POST request "/users"
     *
     * @param userRequestDTO fields of User to be added
     * @return added User
     *
     * @see com.tascigorkem.restaurantservice.api.user.UserResponseDTO
     */
    @PostMapping("/users")
    Mono<Response> addUser(@RequestBody UserRequestDTO userRequestDTO);

    /**
     * Handles the incoming PUT request "/users/{id}"
     *
     * @param id of the User to be updated
     * @param userRequestDTO fields of User to be updated
     * @return updated User
     *
     * @see com.tascigorkem.restaurantservice.api.user.UserResponseDTO
     */
    @PutMapping("/users/{id}")
    Mono<Response> updateUser(@PathVariable("id") UUID id, @RequestBody UserRequestDTO userRequestDTO);

    /**
     * Handles the incoming DELETE request "/users/{id}"
     *
     * @param id of the User to be deleted
     * @return removed User
     *
     * @see com.tascigorkem.restaurantservice.api.user.UserResponseDTO
     */
    @DeleteMapping("/users/{id}")
    Mono<Response> removeUser(@PathVariable("id") UUID id);

    Function<UserDTO, UserResponseDTO> mapToUserResponseDTO();

    Function<UserRequestDTO, UserDTO> mapToUserDto();
}
