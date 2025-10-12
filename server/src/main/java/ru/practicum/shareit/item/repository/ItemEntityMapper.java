package ru.practicum.shareit.item.repository;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.ItemReqDto;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.entity.RequestEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class})
public interface ItemEntityMapper {

    @Mapping(source = "request.id", target = "requestId")
    Item toItem(ItemEntity entity);

    @Mapping(source = "requestId", target = "request")
    ItemEntity toEntity(Item item, @Context UserRepository userRepository);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    void updateEntity(Item item, @MappingTarget ItemEntity itemEntity);

    @Mapping(target = "id", ignore = true)
    CommentEntity toEntity(Comment comment);

    @Mapping(target = "created", expression = "java(entity.getRegistry().toLocalDateTime())")
    Comment toComment(CommentEntity entity);

    List<Comment> toCommentList(List<CommentEntity> entitys);

    ItemReqDto toItemReqDto(ItemEntity itemEntity);

    default RequestEntity map(Long id) {
        if (id == null) return null;
        RequestEntity request = new RequestEntity();
        request.setId(id);
        return request;
    }

    default Long map(RequestEntity request) {
        return request == null ? null : request.getId();
    }
}
