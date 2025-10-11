package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {
    long id;
    String name;
    String description;
    boolean available;
    UserResponse owner;
    ItemRequest request;
    BookingItemDto lastBooking;
    BookingItemDto nextBooking;
    private List<CommentDto> comments;
}
