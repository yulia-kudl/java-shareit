package ru.practicum.shareit.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ProjectException;
import ru.practicum.shareit.user.Service.UserServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.entity.UserEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@Rollback
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void getUser_returnsUser_whenExists() {
        UserEntity entity = new UserEntity();
        entity.setName("Анна");
        entity.setEmail("anna@test.com");
        entityManager.persist(entity);
        entityManager.flush();

        // Вызов сервиса
        User user = userService.getUser(entity.getId());

        // Проверки
        assertThat(user.getId(), equalTo(entity.getId()));
        assertThat(user.getName(), equalTo("Анна"));
        assertThat(user.getEmail(), equalTo("anna@test.com"));
    }

    @Test
    void getUser_throwsException_whenNotFound() {
        // Проверяем, что сервис выбрасывает ProjectException
        ProjectException ex = assertThrows(ProjectException.class,
                () -> userService.getUser(9999L));

        assertThat(ex.getMessage(), containsString("пользователь с id 9999"));
    }
}
