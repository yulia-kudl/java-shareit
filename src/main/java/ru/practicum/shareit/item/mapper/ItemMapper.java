package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toItem(ItemRequestDto request);

    Item toItem(ItemUpdateDto update);

    ItemResponseDto toItemResponse(Item item);

    List<ItemResponseUserDto> toItemResponseList(List<Item> items);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "item", ignore = true)
    Comment toComment(CommentDto dto);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentDto(Comment comment);

}
