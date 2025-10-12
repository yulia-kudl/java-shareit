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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.entity.ItemEntity;
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
class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;

    @PersistenceContext
    private EntityManager entityManager;

    private UserEntity booker;
    private UserEntity owner;
    private ItemEntity item;

    @BeforeEach
    void setUp() {
        booker = new UserEntity();
        booker.setName("Booker");
        booker.setEmail("booker@mail.com");
        entityManager.persist(booker);

        owner = new UserEntity();
        owner.setName("Owner");
        owner.setEmail("owner@mail.com");
        entityManager.persist(owner);

        item = new ItemEntity();
        item.setName("Item1");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);
        entityManager.persist(item);

        entityManager.flush();
    }

    @Test
    void testGetBookingsForBookerByState_AllPastFuture() {
        Booking pastBooking = new Booking(
                null,
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2),
                booker.getId(),
                item.getId(),
                null, null, null
        );

        Booking futureBooking = new Booking(
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                booker.getId(),
                item.getId(),
                null, null, null
        );

        bookingService.addBooking(booker.getId(), pastBooking);
        bookingService.addBooking(booker.getId(), futureBooking);

        List<Booking> allBookings =
                bookingService.getBookingsForBookerByState(booker.getId(), BookingState.ALL);
        assertEquals(2, allBookings.size(), "Ожидалось 2 бронирования в состоянии ALL");

        List<Booking> pastBookings =
                bookingService.getBookingsForBookerByState(booker.getId(), BookingState.PAST);
        assertEquals(1, pastBookings.size(), "Ожидалось одно прошедшее бронирование");
        assertTrue(pastBookings.get(0).getEnd().isBefore(LocalDateTime.now()));

        List<Booking> futureBookings =
                bookingService.getBookingsForBookerByState(booker.getId(), BookingState.FUTURE);
        assertEquals(1, futureBookings.size(), "Ожидалось одно будущее бронирование");
        assertTrue(futureBookings.get(0).getStart().isAfter(LocalDateTime.now()));
    }
}