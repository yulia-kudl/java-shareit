package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.request.dto.ItemRequest;
import ru.practicum.shareit.user.dto.UserResponse;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ItemResponseDto {
    Long id;
    String name;
    String description;
    boolean available;
    UserResponse owner;
    ItemRequest request;
    BookingItemDto lastBooking;
    BookingItemDto nextBooking;
    private List<CommentDto> comments;
}
