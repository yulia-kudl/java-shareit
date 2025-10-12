package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ProjectException;
import ru.practicum.shareit.user.Service.UserServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.repository.UserRepositoryMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRepositoryMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private User user;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Анна");
        userEntity.setEmail("anna@test.com");

        user = new User(1L, "Анна", "anna@test.com");
    }

    @Test
    void getUser_whenUserExists_returnsMappedUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userMapper.toUser(userEntity)).thenReturn(user);

        User result = userService.getUser(1L);

        assertEquals("Анна", result.getName());
        assertEquals("anna@test.com", result.getEmail());
        verify(userRepository, times(2)).findById(1L);
        verify(userMapper, times(1)).toUser(userEntity);

    }

    @Test
    void getUser_whenUserNotFound_throwsException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ProjectException ex = assertThrows(ProjectException.class, () -> userService.getUser(999L));
        assertTrue(ex.getMessage().contains("пользователь с id 999"));

        verify(userRepository).findById(999L);
        verifyNoInteractions(userMapper);
    }
}
