package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}", state, userId);
        return bookingClient.getBookings(userId, state);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    //GET /bookings/{bookingId}
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    //PATCH /bookings/{bookingId}?approved={approved}
    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long bookingId,
                                         @RequestParam boolean approved) {
        log.info(" Update booking status {}, userId={}, approved ={} ", bookingId, ownerId, approved);
        return bookingClient.updateBookingStatus(bookingId, ownerId, approved);
    }

    //GET /bookings/owner?state={state}
    @GetMapping("owner")
    ResponseEntity<Object> getBookingForOwnerByState(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                     @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        log.info(" get booking for userId={}, state ={} ", ownerId, state);
        return bookingClient.getBookingsForOwnerByState(ownerId, state);
    }


}



