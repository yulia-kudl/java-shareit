package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseUserDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toItem(ItemRequestDto request);

    Item toItem(ItemUpdateDto update);

    ItemResponseDto toItemResponse(Item item);

    ItemResponseUserDto toItemResponseUser(Item item);

    List<ItemResponseUserDto> toItemResponseList(List<Item> items);

}
