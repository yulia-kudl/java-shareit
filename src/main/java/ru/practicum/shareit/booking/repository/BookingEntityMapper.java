package ru.practicum.shareit.booking.repository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {Timestamp.class, LocalDateTime.class},
        uses = {UserMapper.class, ItemMapper.class} // для статических методов
)
public interface BookingEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "start", expression = "java(booking.getStart() != null ? Timestamp.valueOf(booking.getStart()) : null)")
    @Mapping(target = "end", expression = "java(booking.getEnd() != null ? Timestamp.valueOf(booking.getEnd()) : null)")
    BookingEntity toEntity(Booking booking);

    @Mapping(target = "booker", source = "user")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "bookerId", ignore = true)
    @Mapping(target = "itemId", ignore = true)
    @Mapping(target = "start", expression = "java(entity.getStart().toLocalDateTime())")
    @Mapping(target = "end", expression = "java(entity.getEnd().toLocalDateTime())")
    Booking toBooking(BookingEntity entity);

}