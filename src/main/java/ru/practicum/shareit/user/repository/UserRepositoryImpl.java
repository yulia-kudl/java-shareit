package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository{
    private final Map<Long, User> users = new HashMap<>();
    private long lastIndex = 0;
    @Override
    public boolean ifUserExists(long userId) {
        return users.containsKey(userId);
    }

    @Override
    public User getUserById(long userId) {
        return users.get(userId);
    }

    @Override
    public User addUser(User request) {
        addId(request);
        users.put(request.getId(), request);
        return request;
    }

    @Override
    public User updateUser(User update, long userId) {
        User currentUser = users.get(userId);
        log.warn("update.getEmail() = '{}'", update.getEmail());
        log.warn("update.getName() = '{}'", update.getName());
        if (update.getEmail()!= null) {
            log.warn(" имэйл новый: {}", update.getEmail());
            currentUser.setEmail(update.getEmail());
        }
        if (update.getName()!= null) {
            log.warn(" name новый: {}", update.getName());
            currentUser.setName(update.getName());
        }
        log.warn(" current name новый: {}", currentUser.getName());

        return currentUser;
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);

    }

    @Override
    public boolean ifEmailExists(String email) {

        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    private void addId(User request) {
        lastIndex++;
        request.setId(lastIndex);
    }
}
