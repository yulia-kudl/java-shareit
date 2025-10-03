package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseUserDto {
    long id;
    String name;
    String description;
    boolean available;
    ItemRequest request;
}
