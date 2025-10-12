package ru.practicum.shareit.booking;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper mapper;

    @PostMapping
    BookingResponseDto create(@RequestBody BookingRequestDto request, @RequestHeader("X-Sharer-User-Id") long bookerId) {
        return mapper.toBookingDto(bookingService.addBooking(bookerId, mapper.toBooking(request)));
    }

    //PATCH /bookings/{bookingId}?approved={approved}
    @PatchMapping("{bookingId}")
    BookingResponseDto update(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long bookingId,
                              @RequestParam boolean approved) {
        return mapper.toBookingDto(bookingService.updateBookingStatus(bookingId, ownerId, approved));
    }

    //GET /bookings/{bookingId}
    @GetMapping("{bookingId}")
    BookingResponseDto getBooking(@RequestHeader("X-Sharer-User-Id") long requester, @PathVariable long bookingId) {
        return mapper.toBookingDto(bookingService.getBooking(requester, bookingId));
    }

    //GET /bookings?state={state}
    @GetMapping
    List<BookingResponseDto> getBookingsForBookerByState(@RequestHeader("X-Sharer-User-Id") long booker,
                                                         @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {

        return bookingService.getBookingsForBookerByState(booker, state).stream()
                .map(mapper::toBookingDto)
                .toList();
    }

    //GET /bookings/owner?state={state}
    @GetMapping("owner")
    List<BookingResponseDto> getBookingForOwnerByState(@RequestHeader("X-Sharer-User-Id") long owner,
                                                       @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        return bookingService.getBookingsForOwnerByState(owner, state).stream()
                .map(mapper::toBookingDto)
                .toList();
    }

}

