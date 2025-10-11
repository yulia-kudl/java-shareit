package ru.practicum.shareit.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository1;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class ItemRequestServiceImplTest {

    @Autowired
    private RequestService itemRequestService;

    @Autowired
    private UserRepository1 userRepository;

    @Autowired
    private RequestRepository requestRepository;

    private UserEntity author;

    @BeforeEach
    void setUp() {
        author = userRepository.save(new UserEntity(1L, "Author", "author@mail.com"));
    }

    @Test
    void testGetRequestsByAuthor_returnsRequestsInDescendingOrder() {
        ItemRequest request1 = new ItemRequest(null, LocalDateTime.now().minusDays(1), null, "ищу дрель", null);
        ItemRequest request2 = new ItemRequest(null, LocalDateTime.now(), null, "ищу молоток", null);

        itemRequestService.addRequest(request1, author.getId());
        itemRequestService.addRequest(request2, author.getId());

        List<ItemRequest> requests = itemRequestService.getRequestsByAuthor(author.getId());

        assertEquals(2, requests.size());
        assertEquals("ищу молоток", requests.get(0).getDescription());
        assertEquals("ищу дрель", requests.get(1).getDescription());
    }

    @Test
    void testGetRequestsByAuthor_noRequests_returnsEmptyList() {
        List<ItemRequest> requests = itemRequestService.getRequestsByAuthor(author.getId());
        assertTrue(requests.isEmpty());
    }

    @Test
    void testGetRequestsByAuthor_nonExistentUser_throwsException() {
        long nonExistentUserId = 999L;
        // addRequest для несуществующего пользователя выбросит NotFoundException
        assertThrows(Exception.class, () -> itemRequestService.addRequest(
                new ItemRequest(null, LocalDateTime.now(), null, "Нужна отвертка", null), nonExistentUserId));
    }
}
