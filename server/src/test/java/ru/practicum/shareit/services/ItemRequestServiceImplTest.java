package ru.practicum.shareit.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@SpringBootTest
class ItemRequestServiceImplTest {

    @Autowired
    private RequestService itemRequestService;

    @PersistenceContext
    private EntityManager entityManager;

    private UserEntity author;

    @BeforeEach
    void setUp() {
        // создаем пользователя напрямую через EntityManager
        author = new UserEntity();
        author.setName("Author");
        author.setEmail("author@mail.com");
        entityManager.persist(author);
        entityManager.flush();
    }

    @Test
    void testGetRequestsByAuthor_returnsRequestsInDescendingOrder() {
        ItemRequest request1 = new ItemRequest(
                null,
                LocalDateTime.now().minusDays(1),
                author.getId(),
                "ищу дрель",
                null
        );

        ItemRequest request2 = new ItemRequest(
                null,
                LocalDateTime.now(),
                author.getId(),
                "ищу молоток",
                null
        );

        itemRequestService.addRequest(request1, author.getId());
        itemRequestService.addRequest(request2, author.getId());

        List<ItemRequest> requests = itemRequestService.getRequestsByAuthor(author.getId());

        assertEquals(2, requests.size(), "Ожидалось две заявки автора");
        assertEquals("ищу молоток", requests.get(0).getDescription(), "Новые заявки должны идти первыми");
        assertEquals("ищу дрель", requests.get(1).getDescription());
    }

    @Test
    void testGetRequestsByAuthor_noRequests_returnsEmptyList() {
        // пользователь есть, но запросов он не создавал
        List<ItemRequest> requests = itemRequestService.getRequestsByAuthor(author.getId());
        assertTrue(requests.isEmpty(), "Ожидался пустой список заявок");
    }

    @Test
    void testAddRequest_nonExistentUser_throwsException() {
        long nonExistentUserId = 999L;

        ItemRequest request = new ItemRequest(
                null,
                LocalDateTime.now(),
                nonExistentUserId,
                "Нужна отвертка",
                null
        );

        assertThrows(Exception.class,
                () -> itemRequestService.addRequest(request, nonExistentUserId),
                "Ожидалось исключение при добавлении запроса несуществующего пользователя");
    }
}