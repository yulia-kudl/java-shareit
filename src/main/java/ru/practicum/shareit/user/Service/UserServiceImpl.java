package ru.practicum.shareit.user.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ProjectException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUser(long userId) {
        if (!(userRepository.ifUserExists(userId))) {
            throw new ProjectException("пользователь с id " + userId + "не найден");
        }
        return userRepository.getUserById(userId);
    }

    @Override
    public User createUser(User request) {
        validate(request.getEmail());
        return userRepository.addUser(request);
    }

    @Override
    public User updateUser(User update, long userId) {
        if (!(userRepository.ifUserExists(userId))) {
            throw new ProjectException("пользователь с id " + userId + "не найден");
        }
        if (update.getEmail() != null) {
            validate(update.getEmail());
        }
        return userRepository.updateUser(update, userId);
    }

    @Override
    public void deleteUser(long userId) {
        if (!(userRepository.ifUserExists(userId))) {
            throw new ProjectException("пользователь с id " + userId + "не найден");
        }
        userRepository.deleteUser(userId);
    }

    private void validate(String email) {
        //проверить что имэйл еще нет
        if (userRepository.ifEmailExists(email)) {
            throw new ProjectException("такой email " + email + " уже существует");
        }
    }
}
