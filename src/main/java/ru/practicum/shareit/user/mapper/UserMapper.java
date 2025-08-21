package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdate;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest request);

    User toUser(UserUpdate update);

    UserResponse toUserResponse(User user);
}
