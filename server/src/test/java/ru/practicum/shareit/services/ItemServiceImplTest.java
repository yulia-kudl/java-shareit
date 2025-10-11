package ru.practicum.shareit.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository1;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository1;

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
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository1 userRepository;

    @Autowired
    private ItemRepository1 itemRepository;

    private UserEntity owner;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(new UserEntity(1L, "Owner", "owner@mail.com"));
    }

    @Test
    void testSearchItems_withText_returnsAvailableItems() {
        ItemEntity item1 = itemRepository.save(new ItemEntity(null, "Комната", "Маленькая студия", true, owner, null));
        ItemEntity item2 = itemRepository.save(new ItemEntity(null, "Дом", "Деревянный дом", true, owner, null));
        ItemEntity item3 = itemRepository.save(new ItemEntity(null, "Коттедж", "Деревянный коттедж", false, owner, null));

        List<Item> result = itemService.searchItems("ком");

        assertEquals(1, result.size());
        assertEquals("Комната", result.get(0).getName());

        List<Item> emptyResult = itemService.searchItems("  ");
        assertTrue(emptyResult.isEmpty());

        List<Item> metalResult = itemService.searchItems("коте");
        assertTrue(metalResult.isEmpty(), "Предмет недоступен");
    }

    @Test
    void testSearchItems_caseInsensitive() {
        ItemEntity item = itemRepository.save(new ItemEntity(null, "Дрель", "Электрическая дрель", true, owner, null));

        List<Item> result = itemService.searchItems("дрЕль"); // проверка нечувствительности к регистру
        assertEquals(1, result.size());
        assertEquals("Дрель", result.get(0).getName());
    }
}
