package ru.practicum.shareit.booking;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper mapper;

    @PostMapping
    BookingDto create(@RequestBody BookingRequestDto request, @RequestHeader("X-Sharer-User-Id") long bookerId) {
        return mapper.toBookingDto(bookingService.addBooking(bookerId, mapper.toBooking(request)));
    }

    //PATCH /bookings/{bookingId}?approved={approved}
    @PatchMapping("{bookingId}")
    BookingDto update(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long bookingId,
                      @RequestParam boolean approved) {
        return mapper.toBookingDto(bookingService.updateBookingStatus(bookingId, ownerId, approved));
    }

    //GET /bookings/{bookingId}
    @GetMapping("{bookingId}")
    BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long requester, @PathVariable long bookingId) {
        return mapper.toBookingDto(bookingService.getBooking(requester, bookingId));
    }

    //GET /bookings?state={state}
    @GetMapping
    List<BookingDto> getBookingsForBookerByState(@RequestHeader("X-Sharer-User-Id") long booker,
                                                 @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {

        return bookingService.getBookingsForBookerByState(booker, state).stream()
                .map(mapper::toBookingDto)
                .toList();
    }

    //GET /bookings/owner?state={state}
    @GetMapping("owner")
    List<BookingDto> getBookingForOwnerByState(@RequestHeader("X-Sharer-User-Id") long owner,
                                               @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        return bookingService.getBookingsForOwnerByState(owner, state).stream()
                .map(mapper::toBookingDto)
                .toList();
    }

}

