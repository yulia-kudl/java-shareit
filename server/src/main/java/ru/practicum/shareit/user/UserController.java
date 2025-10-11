package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.Service.UserService;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.mapper.UserMapper;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserMapper mapper;
    private final UserService service;

    @GetMapping("/{userId}")
    UserResponse getUserById(@PathVariable long userId) {
        return mapper.toUserResponse(service.getUser(userId));
    }

    @PostMapping
    UserResponse createUser(@RequestBody UserRequest request) {
        return mapper.toUserResponse(service.createUser(mapper.toUser(request)));
    }

    @PatchMapping("/{userId}")
    UserResponse updateUser(@RequestBody UserUpdate update, @PathVariable long userId) {
        return mapper.toUserResponse(service.updateUser(mapper.toUser(update), userId));
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable long userId) {
        service.deleteUser(userId);
    }


}
