package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingState;

import java.util.List;

@Service
public interface BookingService {

    List<Booking> getBookingsForBookerByState(long booker, BookingState state);

    Booking addBooking(long bookerId, Booking booking);

    Booking updateBookingStatus(long bookingId, long ownerId, boolean approved);

    Booking getBooking(long requester, long bookingId);

    List<Booking> getBookingsForOwnerByState(long owner, BookingState state);


    Booking getNextBookingForItem(long itemId);

    Booking getLastBookingForItem(long itemId);
}
