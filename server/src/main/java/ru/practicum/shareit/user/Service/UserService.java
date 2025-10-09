package ru.practicum.shareit.user.Service;

import ru.practicum.shareit.user.User;

public interface UserService {
    User getUser(long userId);

    User createUser(User user);

    User updateUser(User user, long userId);

    void deleteUser(long userId);
}
