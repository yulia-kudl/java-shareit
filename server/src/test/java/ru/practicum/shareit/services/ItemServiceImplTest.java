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
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.entity.UserEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Rollback
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@SpringBootTest
class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @PersistenceContext
    private EntityManager entityManager;

    private UserEntity owner;

    @BeforeEach
    void setUp() {
        owner = new UserEntity();
        owner.setName("Owner");
        owner.setEmail("owner@mail.com");
        entityManager.persist(owner);
        entityManager.flush();
    }

    @Test
    void testSearchItems_withText_returnsAvailableItems() {
        ItemEntity item1 = new ItemEntity(null, "Комната", "Маленькая студия", true, owner, null);
        ItemEntity item2 = new ItemEntity(null, "Дом", "Деревянный дом", true, owner, null);
        ItemEntity item3 = new ItemEntity(null, "Коттедж", "Деревянный коттедж", false, owner, null);

        entityManager.persist(item1);
        entityManager.persist(item2);
        entityManager.persist(item3);
        entityManager.flush();

        // Тест поиска по слову "ком"
        List<Item> result = itemService.searchItems("ком");
        assertEquals(1, result.size(), "Должен быть найден только один доступный предмет");
        assertEquals("Комната", result.get(0).getName());

        // Тест пустого запроса — должен вернуть пустой список
        List<Item> emptyResult = itemService.searchItems("  ");
        assertTrue(emptyResult.isEmpty(), "Пустая строка не должна возвращать результаты");

        // Тест, что недоступный предмет не попадает в поиск
        List<Item> unavailableResult = itemService.searchItems("коте");
        assertTrue(unavailableResult.isEmpty(), "Недоступный предмет не должен возвращаться в результатах");
    }

    @Test
    void testSearchItems_caseInsensitive() {
        ItemEntity item = new ItemEntity(null, "Дрель", "Электрическая дрель", true, owner, null);
        entityManager.persist(item);
        entityManager.flush();

        // Проверка нечувствительности к регистру
        List<Item> result = itemService.searchItems("дрЕль");
        assertEquals(1, result.size(), "Поиск должен быть нечувствителен к регистру");
        assertEquals("Дрель", result.get(0).getName());
    }
}
