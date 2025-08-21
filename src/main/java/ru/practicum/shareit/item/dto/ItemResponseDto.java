package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {
    long id;
    String name;
    String description;
    boolean available;
    long owner;
    ItemRequest request;
}
