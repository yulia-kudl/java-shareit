package ru.practicum.shareit.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ProjectException;
import ru.practicum.shareit.user.Service.UserServiceImpl;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.repository.UserRepository1;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@Rollback
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class UserServiceImplGetUserTest {

    private final UserServiceImpl service;
    private final UserRepository1 userRepository;
    private final UserMapper mapper;

    @PersistenceContext
    private EntityManager em;

    @Test
    void getUser_returnsUser_whenExists() {
        UserEntity entity = new UserEntity();
        entity.setName("Анна");
        entity.setEmail("anna@test.com");
        UserEntity savedEntity = userRepository.save(entity);

        User user = service.getUser(savedEntity.getId());

        assertThat(user.getId(), equalTo(savedEntity.getId()));
        assertThat(user.getName(), equalTo("Анна"));
        assertThat(user.getEmail(), equalTo("anna@test.com"));
    }

    @Test
    void getUser_throwsException_whenNotFound() {

        ProjectException ex = assertThrows(ProjectException.class,
                () -> service.getUser(9999L));

        assertThat(ex.getMessage(), containsString("пользователь с id 9999"));
    }
}
