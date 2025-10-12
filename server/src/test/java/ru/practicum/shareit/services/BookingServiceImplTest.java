package ru.practicum.shareit.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Rollback
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private UserEntity booker;
    private UserEntity owner;
    private ItemEntity item;

    @BeforeEach
    void setUp() {
        booker = userRepository.save(new UserEntity(1, "Booker", "booker@mail.com"));
        owner = userRepository.save(new UserEntity(2, "Owner", "owner@mail.com"));

        item = itemRepository.save(new ItemEntity(null, "Item1", "Description", true, owner, null));

    }

    @Test
    void testGetBookingsForBookerByState_All() {
        Booking booking1 = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1),
                booker.getId(), item.getId(), null, null, null);
        Booking booking2 = new Booking(2L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                booker.getId(), item.getId(), null, null, null);

        bookingService.addBooking(booker.getId(), booking1);
        bookingService.addBooking(booker.getId(), booking2);

        // проверяем ALL
        List<Booking> allBookings = bookingService.getBookingsForBookerByState(booker.getId(), BookingState.ALL);
        assertEquals(2, allBookings.size());

        // проверяем PAST
        List<Booking> pastBookings = bookingService.getBookingsForBookerByState(booker.getId(), BookingState.PAST);
        assertEquals(1, pastBookings.size());
        assertTrue(pastBookings.get(0).getStart().isBefore(LocalDateTime.now()));

        // проверяем FUTURE
        List<Booking> futureBookings = bookingService.getBookingsForBookerByState(booker.getId(), BookingState.FUTURE);
        assertEquals(1, futureBookings.size());
        assertTrue(futureBookings.get(0).getStart().isAfter(LocalDateTime.now()));
    }
}
