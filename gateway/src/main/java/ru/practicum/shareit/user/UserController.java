package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.Service.UserService;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.mapper.UserMapper;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    ResponseEntity<Object> getUserById(@PathVariable long userId) {
        return userClient.getUser(userId);
    }

    @PostMapping
    ResponseEntity<Object> createUser(@Valid @RequestBody UserRequest request) {
        return userClient.createUser(request);
    }

    @PatchMapping("/{userId}")
    ResponseEntity<Object> updateUser(@Valid @RequestBody UserUpdate update, @PathVariable long userId) {
        return userClient.updateUser(userId, update);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable long userId) {

        userClient.deleteUser(userId);
    }


}
