package ru.practicum.shareit.user.repository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.entity.UserEntity;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserRepositoryMapper {

    User toUser(UserEntity userEntity);

    UserEntity toEntity(User user);

    @Mapping(target = "id", ignore = true)
    void updateEntity(User user, @MappingTarget UserEntity userEntity);

}
