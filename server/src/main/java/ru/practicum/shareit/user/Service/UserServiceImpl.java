package ru.practicum.shareit.user.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ProjectException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.repository.UserRepositoryMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRepositoryMapper mapper;

    @Override
    public User getUser(long userId) {
        if ((userRepository.findById(userId).isEmpty())) {
            throw new ProjectException("пользователь с id " + userId + "не найден");
        }
        return mapper.toUser(userRepository.findById(userId).get());
    }

    @Override
    public User createUser(User request) {
        validate(request.getEmail());
        return mapper.toUser(userRepository.save(mapper.toEntity(request)));
    }

    @Override
    public User updateUser(User update, long userId) {
        if (!(userRepository.existsById(userId))) {
            throw new ProjectException("пользователь с id " + userId + "не найден");
        }
        if (update.getEmail() != null) {
            validate(update.getEmail());
        }
        UserEntity userToUpdate = userRepository.findById(userId).get();
        mapper.updateEntity(update, userToUpdate);
        return mapper.toUser(userRepository.save(userToUpdate));
    }

    @Override
    public void deleteUser(long userId) {
        if (!(userRepository.existsById(userId))) {
            throw new ProjectException("пользователь с id " + userId + "не найден");
        }
        userRepository.deleteById(userId);
    }


    private void validate(String email) {
        //проверить что имэйл еще нет
        if (userRepository.existsByEmail(email)) {
            throw new ProjectException("такой email " + email + " уже существует");
        }
    }
}
