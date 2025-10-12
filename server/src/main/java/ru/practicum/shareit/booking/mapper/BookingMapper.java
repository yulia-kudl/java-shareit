package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingResponseDto toBookingDto(Booking booking);

    Booking toBooking(BookingResponseDto bookingResponseDto);

    Booking toBooking(BookingRequestDto requestDto);

    BookingItemDto toItemBooking(Booking booking);
}
