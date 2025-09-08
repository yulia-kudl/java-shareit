package ru.practicum.shareit.item.repository;

import org.mapstruct.*;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository1;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class})
public interface ItemEntityMapper {

    Item toItem(ItemEntity entity);

    ItemEntity toEntity(Item item, @Context UserRepository1 userRepository);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    void updateEntity(Item item, @MappingTarget ItemEntity itemEntity);

    @Mapping(target = "id", ignore = true)
    CommentEntity toEntity(Comment comment);

    @Mapping(target = "created", expression = "java(entity.getRegistry().toLocalDateTime())")
    Comment toComment(CommentEntity entity);
    List<Comment> toCommentList(List<CommentEntity> entitys);
}
