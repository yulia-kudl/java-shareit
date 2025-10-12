package ru.practicum.shareit.request.repository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.entity.RequestEntity;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class})
public interface RequestEntityMapper {


    public RequestEntity toEntity(ItemRequest itemRequest);

    @Mapping(source = "author.id", target = "authorId")
    public ItemRequest toItemRequest(RequestEntity entity);
}
