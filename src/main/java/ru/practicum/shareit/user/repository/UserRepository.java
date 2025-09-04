package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

@Repository
public interface UserRepository {
    boolean ifUserExists(long userId);

    User getUserById(long userId);

    User addUser(User request);

    User updateUser(User update, long userId);

    void deleteUser(long userId);

    boolean ifEmailExists(String email);
}
